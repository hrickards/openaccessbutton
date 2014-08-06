package org.openaccessbutton.openaccessbutton;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Allows fragments to launch other fragments
 */
public interface OnFragmentNeededListener {
    public void launchFragment(Fragment fragment, String tag, Bundle data, boolean backstack);
}
