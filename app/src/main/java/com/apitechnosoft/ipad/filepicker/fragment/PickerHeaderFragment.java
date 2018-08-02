package com.apitechnosoft.ipad.filepicker.fragment;

import android.view.View;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.filepicker.FNFilePicker;
import com.apitechnosoft.ipad.component.ASTTextView;
import com.apitechnosoft.ipad.filepicker.listener.IToolbarListener;
import com.apitechnosoft.ipad.filepicker.view.ASTCropImageView;
import com.apitechnosoft.ipad.fragment.HeaderFragment;
import com.apitechnosoft.ipad.utils.ASTStringUtil;
import static com.apitechnosoft.ipad.utils.ASTObjectUtil.isEmptyStr;
import static com.apitechnosoft.ipad.utils.ASTObjectUtil.isNonEmptyStr;


/**
 * Created 15-06-2017
 *
 * @author AST Inc.
 */
public class PickerHeaderFragment extends HeaderFragment {

    private View rotateLeft;
    private View rotateRight;
    private View cameraButton;
    private ASTTextView doneButton;
    private View cropButton;

    private boolean isShowDone;
    private boolean isShowRotateButton;
    private boolean isShowCamera;
    private boolean isShowCrop;
    private String headerTitle;

    private String doneButtonText;

    @Override
    protected void getArgs() {
        setShowDone(getArguments().getBoolean(FNFilePicker.EXTRA_SHOW_DONE, false));
        setShowRotateButton(getArguments().getBoolean(FNFilePicker.EXTRA_SHOW_ROTATE, false));
        setShowCamera(getArguments().getBoolean(FNFilePicker.EXTRA_SHOW_CAMERA, false));
        setShowCrop(getArguments().getBoolean(FNFilePicker.EXTRA_SHOW_CROP, false));
        setHeaderTitle(getArguments().getString(FNFilePicker.EXTRA_HEADER_TITLE, ""));
        doneButtonText = getArguments().getString(FNFilePicker.EXTRA_DONE_TEXT, ASTStringUtil.getStringForID(R.string.text_done));
    }

    @Override
    protected int fragmentLayout() {
        return R.layout.picker_header;
    }

    @Override
    protected void loadView() {
        this.backButton = this.findViewById(R.id.backBtn);
        this.rotateLeft = this.findViewById(R.id.btnRotateLeft);
        this.rotateRight = this.findViewById(R.id.btnRotateRight);
        this.cameraButton = this.findViewById(R.id.cameraButton);
        this.doneButton = this.findViewById(R.id.doneButton);
        this.cropButton = this.findViewById(R.id.cropButton);
        this.title = this.findViewById(R.id.title);

        this.doneButton.setText(doneButtonText);
    }

    @Override
    protected void setClickListeners() {
        this.backButton.setOnClickListener(this);
        this.rotateLeft.setOnClickListener(this);
        this.rotateRight.setOnClickListener(this);
        this.cameraButton.setOnClickListener(this);
        this.doneButton.setOnClickListener(this);
        this.cropButton.setOnClickListener(this);
    }

    @Override
    protected void setAccessibility() {
        rotateLeft.setVisibility(isShowRotateButton() ? View.VISIBLE : View.GONE);
        rotateRight.setVisibility(isShowRotateButton() ? View.VISIBLE : View.GONE);
        cameraButton.setVisibility(isShowCamera() ? View.VISIBLE : View.GONE);
        doneButton.setVisibility(isShowDone() ? View.VISIBLE : View.GONE);
        cropButton.setVisibility(isShowCrop() ? View.VISIBLE : View.GONE);
        title.setVisibility(isEmptyStr(headerTitle) ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void dataToView() {
        if (isNonEmptyStr(getHeaderTitle())) {
            this.title.setVisibility(View.VISIBLE);
            this.title.setText(getHeaderTitle());
        }
    }


    @Override
    public void onClick(View v) {
        IToolbarListener listener = toolbarListener();
        if (listener == null) {
            return;
        }
        if (v.getId() == R.id.cameraButton) {
            listener.onCameraClick();
        } else if (v.getId() == R.id.btnRotateLeft) {
            listener.rotateImage(ASTCropImageView.RotateDegrees.ROTATE_M90D);
        } else if (v.getId() == R.id.btnRotateRight) {
            listener.rotateImage(ASTCropImageView.RotateDegrees.ROTATE_90D);
        } else if (v.getId() == R.id.cropButton) {
            listener.openCropFragment();
        } else if (v.getId() == R.id.doneButton) {
            listener.onDone();
        } else if (v.getId() == R.id.backBtn) {
            getHostActivity().onBackPressed();
        }
    }


    @Override
    public void updateTitle() {
        setAccessibility();
        if (this.title == null) {
            return;
        }
        if (isNonEmptyStr(getHeaderTitle())) {
            this.title.setVisibility(View.VISIBLE);
            this.title.setText(getHeaderTitle());
        }
    }

    private IToolbarListener toolbarListener() {
        return getHostActivity() != null ? (IToolbarListener) getHostActivity() : null;
    }

    public boolean isShowDone() {
        return isShowDone;
    }

    public void setShowDone(boolean showDone) {
        isShowDone = showDone;
    }

    public boolean isShowRotateButton() {
        return isShowRotateButton;
    }

    public void setShowRotateButton(boolean showRotateButton) {
        isShowRotateButton = showRotateButton;
    }

    public boolean isShowCamera() {
        return isShowCamera;
    }

    public void setShowCamera(boolean showCamera) {
        isShowCamera = showCamera;
    }

    public boolean isShowCrop() {
        return isShowCrop;
    }

    public void setShowCrop(boolean showCrop) {
        isShowCrop = showCrop;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }
}
