package com.edibusl.listeatapp.helpers;

import android.app.Activity;
import android.content.Context;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;


public class AwsUtils {
    private static AwsUtils mInstance;
    private static final String S3_FOLDER = "public";

    private Activity mCurContext;

    private AwsUtils() {}
    public static synchronized AwsUtils getInstance() {
        if (mInstance == null) {
            mInstance = new AwsUtils();
        }
        return mInstance;
    }

    public void uploadData(Activity activityContext, final String fileFullPath, final String filename) {
        mCurContext = activityContext;
        AWSMobileClient.getInstance().initialize(activityContext, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult res) {
                uploadWithTransferUtility(fileFullPath, filename);
            }
        }).execute();
    }

    public void uploadWithTransferUtility(final String fileFullPath, final String filename) {
        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(mCurContext)
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider()))
                        .build();

        TransferObserver uploadObserver =
                transferUtility.upload(
                        String.format("%s/%s", S3_FOLDER, filename),
                        new File(fileFullPath));

        // Attach a listener to the observer to get notified of the
        // updates in the state and the progress
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    //TODO -
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                //TODO
//                Log.d("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
//                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // TODO - Handle errors
                int x = 1;
                x++;
            }

        });

        // If you do not want to attach a listener and poll for the data
        // from the observer, you can check for the state and the progress
        // in the observer.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
        }

//        Log.d("YourActivity", "Bytes Transferrred: " + uploadObserver.getBytesTransferred());
//        Log.d("YourActivity", "Bytes Total: " + uploadObserver.getBytesTotal());
    }
}
