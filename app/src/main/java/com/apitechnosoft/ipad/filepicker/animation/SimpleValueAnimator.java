package com.apitechnosoft.ipad.filepicker.animation;

/**
 * Created 15-06-2017
 *
 * @author AST Inc.
 */
public interface SimpleValueAnimator {
	void startAnimation(long duration);

	void cancelAnimation();

	boolean isAnimationStarted();

	void addAnimatorListener(SimpleValueAnimatorListener animatorListener);
}
