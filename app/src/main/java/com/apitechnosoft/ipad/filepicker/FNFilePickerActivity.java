package com.apitechnosoft.ipad.filepicker;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.Formatter;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.MainActivity;
import com.apitechnosoft.ipad.filepicker.fragment.DocumentFragment;
import com.apitechnosoft.ipad.filepicker.fragment.FNCropImageFragment;
import com.apitechnosoft.ipad.filepicker.fragment.MediaFragment;
import com.apitechnosoft.ipad.filepicker.fragment.PickerHeaderFragment;
import com.apitechnosoft.ipad.filepicker.helper.BitmapSaveTask;
import com.apitechnosoft.ipad.filepicker.helper.ASTImageUtil;
import com.apitechnosoft.ipad.filepicker.listener.CropCallback;
import com.apitechnosoft.ipad.filepicker.listener.IPageListener;
import com.apitechnosoft.ipad.filepicker.listener.IToolbarListener;
import com.apitechnosoft.ipad.filepicker.model.MediaFile;
import com.apitechnosoft.ipad.filepicker.view.ASTCropImageView;
import com.apitechnosoft.ipad.fragment.MainFragment;
import com.apitechnosoft.ipad.utils.ASTReqResCode;
import com.apitechnosoft.ipad.utils.ASTStringUtil;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.FNReqResCode;

import java.util.ArrayList;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.apitechnosoft.ipad.filepicker.FNFilePicker.EXTRA_SELECTED_MEDIA;
import static com.apitechnosoft.ipad.filepicker.FNFilePicker.MODE_MULTIPLE;
import static com.apitechnosoft.ipad.filepicker.FNFilePicker.MODE_SINGLE;
import static com.apitechnosoft.ipad.utils.FNObjectUtil.isEmpty;

/**
 * Created 05-06-2017
 *
 * @author AST Inc.
 */
public class FNFilePickerActivity extends MainActivity implements IPageListener, IToolbarListener {

    private FNFilePickerConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_activity);
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null) {
            finish();
            return;
        }
        config = intent.getExtras().getParcelable(FNFilePickerConfig.class.getSimpleName());
        if (config == null) {
            config = ASTImageUtil.makeConfigFromIntent(this, intent);
        }

        redirectToHomeMenu();
    }


    @Override
    public void permissionDenied(int requestCode) {
        if (requestCode == ASTReqResCode.PERMISSION_REQ_WRITE_EXTERNAL_STORAGE) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }


    @Override
    public void redirectToHomeMenu() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(FNFilePicker.EXTRA_SHOW_CAMERA, showCamera());
        bundle.putBoolean(FNFilePicker.EXTRA_SHOW_DONE, config.getSelectedFiles().size() > 0);
        bundle.putBoolean(FNFilePicker.EXTRA_SHOW_CROP, isShowCrop());
        bundle.putString(FNFilePicker.EXTRA_HEADER_TITLE, headerTitle());
        MainFragment fragment = config.getMediaType() == FNFilePicker.MEDIA_TYPE_DOCUMENT ? new DocumentFragment() : new MediaFragment();
        updateFragment(fragment, bundle, true, false);
    }

    @Override
    public void permissionGranted(int requestCode) {
        super.permissionGranted(requestCode);
    }

    @Override
    protected MainFragment headerFragmentInstance() {
        return new PickerHeaderFragment();
    }

    @Override
    public MainFragment getPageFragment() {
        return dataContainer();
    }


    private String headerTitle() {
        if (isCropPage()) {
            return null;
        }
        String headertTitle = ASTStringUtil.getStringForID(R.string.select_image);
        if (config.getSelectedFiles().size() > 0 && config.getMode() == MODE_MULTIPLE) {
            headertTitle = String.format(getString(R.string.text_selected), config.getSelectedFiles().size());
        }
        return headertTitle;
    }

    private boolean isShowCrop() {
        return config.getMode() == MODE_SINGLE && !config.isReturnAfterFirst() && config.getSelectedFiles().size() > 0 &&
                config.getMediaType() == MEDIA_TYPE_IMAGE;
    }

    private boolean isCropPage() {
        MainFragment currentFragment = getPageFragment();
        return currentFragment != null && currentFragment instanceof FNCropImageFragment;
    }

    @Override
    public boolean isValidToAddFile() {
        if (config.getMode() == MODE_MULTIPLE && config.isOverLimit()) {
            ASTUIUtil.showErrorIndicator(this, String.format(ASTStringUtil.getStringForID(R.string.error_image_select_limit), config.getLimit()), headerView());
            return false;
        }
        return true;
    }

    @Override
    public FNFilePickerConfig config() {
        return config;
    }

    @Override
    public void onBackPressed() {
        if (this.getPageFragment() != null && !this.getPageFragment().onBackPressed()) {
            return;
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void openCropFragment(MediaFile file) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("imageFile", file);
        bundle.putBoolean(FNFilePicker.EXTRA_SHOW_ROTATE, true);
        bundle.putBoolean(FNFilePicker.EXTRA_SHOW_DONE, true);
        bundle.putString(FNFilePicker.EXTRA_DONE_TEXT, ASTStringUtil.getStringForID(R.string.save));
        bundle.putBoolean(FNFilePicker.EXTRA_CROP_MODE, (config.getMode() == MODE_SINGLE && config.isReturnAfterFirst()));
        updateFragment(new FNCropImageFragment(), bundle, true, false);
    }

    private PickerHeaderFragment pickerHeaderFragment() {
        return (PickerHeaderFragment) headerFragment();
    }

    public void updateHeader() {
        if (config.getMode() == MODE_SINGLE && config.isReturnAfterFirst()) {
            openCropFragment(config.getSelectedFiles().get(0));
            return;
        }
        PickerHeaderFragment headerFragment = pickerHeaderFragment();
        if (headerFragment == null) {
            return;
        }
        headerFragment.setShowCamera(showCamera() && !isCropPage());
        headerFragment.setHeaderTitle(!isCropPage() ? headerTitle() : ASTStringUtil.getStringForID(R.string.save));
        headerFragment.setShowCrop(isShowCrop() && !isCropPage());
        headerFragment.setShowDone(config.getSelectedFiles().size() > 0);
        headerFragment.setShowRotateButton(isCropPage());
        headerFragment.updateTitle();
    }

    private boolean showCamera() {
        return config.isShowCamera() && config.getMediaType() == MEDIA_TYPE_IMAGE;
    }

    @Override
    public void onDone() {
        if (config.getMode() == MODE_SINGLE && config.isReturnAfterFirst() && !isCropPage()) {
            openCropFragment();
            return;
        }
        if (!isEmpty(this.config.getSelectedFiles())) {
            for (int i = 0; i < this.config.getSelectedFiles().size(); i++) {
                MediaFile image = this.config.getSelectedFiles().get(i);
                if (image.getFilePath() == null || !image.getFilePath().exists()) {
                    this.config.getSelectedFiles().remove(i);
                    i--;
                }
            }
        }
        if (config.getMode() == MODE_SINGLE && isCropPage()) {
            ((FNCropImageFragment) getPageFragment()).startCrop(new CropCallback() {

                @Override
                public void onSuccess(MediaFile file) {
                    if (config.getSelectedFiles().size() > 0) {
                        config.getSelectedFiles().clear();
                    }
                    config.getSelectedFiles().add(file);
                    saveAndFinish();
                }

                @Override
                public void onError() {

                }
            });
        } else if (!isEmpty(this.config.getSelectedFiles())) {
            saveAndFinish();
        }
    }

    private void saveAndFinish() {
        BitmapSaveTask saveTask = new BitmapSaveTask(this) {
            @Override
            protected void onPostExecute(ArrayList<MediaFile> deviceFiles) {
                super.onPostExecute(deviceFiles);
                if (!isWithinSizeLimit()) {
                    return;
                }
                Intent data = new Intent();
                data.putParcelableArrayListExtra(EXTRA_SELECTED_MEDIA, config.getSelectedFiles());
                setResult(RESULT_OK, data);
                finish();
            }
        };
        saveTask.execute(config.getSelectedFiles());
    }

    @Override
    public void onCameraClick() {
        requestPermission(FNReqResCode.PERMISSION_REQ_CAMERA);
    }

    @Override
    public void rotateImage(ASTCropImageView.RotateDegrees degrees) {
        if (isCropPage()) {
            ((FNCropImageFragment) getPageFragment()).rotateImage(degrees);
        }
    }

    @Override
    public void openCropFragment() {
        openCropFragment(config.getSelectedFiles().get(0));
    }

    private boolean isWithinSizeLimit() {
        if (config.isOverLimitSize()) {
            ASTUIUtil.showErrorIndicator(this, String.format(ASTStringUtil.getStringForID(R.string.error_image_select_size_limit), Formatter.formatFileSize(this, config.getSizeLimit())), headerView());
            return false;
        }
        return true;
    }
}
