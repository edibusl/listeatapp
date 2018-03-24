package com.edibusl.listeatapp.helpers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

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
    private static final String LOG_TAG = "AwsUtils";
    private static final String S3_FOLDER = "public";

    private Activity mCurContext;

    //Singleton declarations
    private static AwsUtils mInstance;
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
                    Log.i(LOG_TAG, "Completed uploading file");
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int)percentDonef;
                Log.d(LOG_TAG, "ID:" + id + " bytesCurrent: " + bytesCurrent + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e(LOG_TAG,"Error when uploading file to S3: " + ex.toString());
            }
        });
    }
}
