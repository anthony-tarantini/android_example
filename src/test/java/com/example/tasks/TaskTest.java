package com.example.tasks;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", emulateSdk = 18)
public class TaskTest {

    @Test
    public void onCreateWithParcel_readsString() {
        final Parcel mockParcel = mock(Parcel.class);
        new TestTask(mockParcel);
        verify(mockParcel).readString();
    }

    @Test
    public void writeToParcel_writesOutIdentifier() {
        final Parcel mockParcel = mock(Parcel.class);
        final TestTask task = new TestTask() {
            @Override
            public String createIdentifier() {
                return "identifier";
            }
        };

        task.writeToParcel(mockParcel, 0);
        verify(mockParcel).writeString("identifier");
    }

    private class TestTask extends Task<Object> {
        public TestTask() {
            super(Robolectric.application);
        }

        public TestTask(final Parcel mockParcel) {
            super(mockParcel);
        }

        @Override
        public Object executeNetworking() throws Exception {
            return null;
        }

        @Override
        public void processResponse(final Object response) throws Exception {

        }

        @Override
        public void notifySuccess() {

        }

        @Override
        public void notifyFailure() {

        }

        @Override
        public void inject() {

        }

        @Override
        public String createIdentifier() {
            return null;
        }
    }
}