/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.gestures;

import static com.android.settings.core.BasePreferenceController.AVAILABLE;
import static com.android.settings.core.BasePreferenceController.UNSUPPORTED_ON_DEVICE;

import static com.google.common.truth.Truth.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.android.internal.hardware.AmbientDisplayConfiguration;
import com.android.settings.aware.AwareFeatureProvider;
import com.android.settings.testutils.FakeFeatureFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class WakeScreenGesturePreferenceControllerTest {

    private static final String KEY_WAKE_SCREEN = "gesture_wake_screen";

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Context mContext;
    @Mock
    private AmbientDisplayConfiguration mAmbientDisplayConfiguration;
    private WakeScreenGesturePreferenceController mController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        AwareFeatureProvider featureProvider =
                FakeFeatureFactory.setupForTest().getAwareFeatureProvider();
        when(featureProvider.isSupported(any())).thenReturn(true);
        when(featureProvider.isEnabled(any())).thenReturn(true);
        mController = new WakeScreenGesturePreferenceController(mContext, KEY_WAKE_SCREEN);
        mController.setConfig(mAmbientDisplayConfiguration);
    }

    @Test
    public void testIsChecked_configIsSet_shouldReturnTrue() {
        // Set the setting to be enabled.
        when(mAmbientDisplayConfiguration.wakeScreenGestureEnabled(anyInt())).thenReturn(true);
        assertThat(mController.isChecked()).isTrue();
    }

    @Test
    public void testIsChecked_configIsNotSet_shouldReturnFalse() {
        // Set the setting to be disabled.
        when(mAmbientDisplayConfiguration.wakeScreenGestureEnabled(anyInt())).thenReturn(false);
        assertThat(mController.isChecked()).isFalse();
    }

    @Test
    public void getAvailabilityStatus_gestureNotSupported_UNSUPPORTED_ON_DEVICE() {
        when(mAmbientDisplayConfiguration.wakeScreenGestureAvailable()).thenReturn(false);
        final int availabilityStatus = mController.getAvailabilityStatus();

        assertThat(availabilityStatus).isEqualTo(UNSUPPORTED_ON_DEVICE);
    }

    @Test
    public void getAvailabilityStatus_gestureSupported_AVAILABLE() {
        when(mAmbientDisplayConfiguration.wakeScreenGestureAvailable()).thenReturn(true);
        final int availabilityStatus = mController.getAvailabilityStatus();

        assertThat(availabilityStatus).isEqualTo(AVAILABLE);
    }

    @Test
    public void isSliceableCorrectKey_returnsTrue() {
        final WakeScreenGesturePreferenceController controller =
                new WakeScreenGesturePreferenceController(mContext, "gesture_wake_screen");
        assertThat(controller.isSliceable()).isTrue();
    }

    @Test
    public void isSliceableIncorrectKey_returnsFalse() {
        final WakeScreenGesturePreferenceController controller =
                new WakeScreenGesturePreferenceController(mContext, "bad_key");
        assertThat(controller.isSliceable()).isFalse();
    }
}
