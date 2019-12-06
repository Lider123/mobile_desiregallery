package com.example.desiregallery.ui.screens.profile

import android.os.Build
import android.transition.Fade
import android.transition.Slide
import android.transition.Transition
import android.view.Gravity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * @author Konstantin on 10.11.2019
 */
class ModalFragmentFactory(private val fragmentManager: FragmentManager) {

    fun create(child: Fragment, containerId: Int): ModalPlaceholderFragment {
        val modalFragment = ModalPlaceholderFragment()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            modalFragment.enterTransition = Fade(Fade.IN).apply {
                addListener(object : Transition.TransitionListener {

                    override fun onTransitionEnd(transition: Transition?) {
                        fragmentManager
                            .beginTransaction()
                            .add(containerId, child)
                            .commit()
                        removeListener(this)
                    }

                    override fun onTransitionResume(transition: Transition?) = Unit

                    override fun onTransitionPause(transition: Transition?) = Unit

                    override fun onTransitionCancel(transition: Transition?) = Unit

                    override fun onTransitionStart(transition: Transition?) = Unit
                })
            }
            modalFragment.exitTransition = Fade(Fade.OUT)
            child.enterTransition = Slide(Gravity.TOP)
            child.exitTransition = Slide(Gravity.TOP).apply {
                addListener(object : Transition.TransitionListener {

                    override fun onTransitionEnd(transition: Transition?) {
                        fragmentManager
                            .beginTransaction()
                            .remove(modalFragment)
                            .commit()
                        removeListener(this)
                    }

                    override fun onTransitionResume(transition: Transition?) = Unit

                    override fun onTransitionPause(transition: Transition?) = Unit

                    override fun onTransitionCancel(transition: Transition?) = Unit

                    override fun onTransitionStart(transition: Transition?) = Unit
                })
            }
        } // TODO: add support for old versions
        return modalFragment
    }
}
