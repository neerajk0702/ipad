package com.apitechnosoft.ipad.filepicker.animation;

/**
 * Created 15-06-2017
 *
 * @author AST Inc.
 */
public interface SimpleValueAnimatorListener {
	void onAnimationStarted();

	void onAnimationUpdated(float scale);

	void onAnimationFinished();
}
