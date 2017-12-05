package li.zoss.bfh.bsc.nearby_sender;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.Payload;

import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

public class MainActivity extends ConnectionsActivity {

    private String TAG = "MainActivity";

    //Used for ConnectionsActivity
    private static final String SERVICE_ID = "li.zoss.bfh.bsc";
    private final String NAME = "Sender " + UUID.randomUUID();
    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;
    private static final String[] REQUIRED_PERMISSIONS =
            new String[] {
                    Manifest.permission.RECORD_AUDIO
            };

    //View
    private TextView txtID;
    private TextView txtState;
    private TextView txtConnectedClients;
    private TextView txtLog;
    private FloatingActionButton btnFloating;

    private State mState = State.UNKNOWN;
    private boolean googleApiClientIsReady = false;
    private AudioRecorder mRecorder;
    private boolean isPublishing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //View
        txtID = findViewById(R.id.txtIDvalue);
        txtState = findViewById(R.id.txtStatevalue);
        txtConnectedClients = findViewById(R.id.txtConnectedDevices);
        txtLog = findViewById(R.id.txtLog);
        btnFloating = findViewById(R.id.floatingActionButton);
        txtConnectedClients.setText("no Clients connected");
        txtLog.setText("Log:");
        txtID.setText(NAME);
        txtState.setText(mState.toString());
        btnFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishInformation();
                    }
                });
            }
        });
        setState(State.UNKNOWN);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void refreshConnectedClients() {
        txtConnectedClients.setText("");
        Iterator<Endpoint> iterator = getConnectedEndpoints().iterator();
        while (iterator.hasNext()) {
            String endpoint = iterator.next().getName();
            txtConnectedClients.append(endpoint + "\n");
        }
    }

    private void getConnectionInfo(Endpoint endpoint){

    }

    private void publishInformation() {
        if (!hasPermissions(this, REQUIRED_PERMISSIONS)) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
        }
        setIsPublishing(true);
        Log.v(TAG, "publishInformation()");
        try {
            ParcelFileDescriptor[] payloadPipe = ParcelFileDescriptor.createPipe();

            // Send the first half of the payload (the read side) to Nearby Connections.
            send(Payload.fromStream(payloadPipe[0]));

            // Use the second half of the payload (the write side) in AudioRecorder.
            mRecorder = new AudioRecorder(payloadPipe[1]);
            mRecorder.start();
        } catch (IOException e) {
            Log.e(TAG, "publishInformation failed", e);
        }
    }
    /** The user has accepted (or denied) our permission request. */
    @CallSuper
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_REQUIRED_PERMISSIONS) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, R.string.error_missing_permissions, Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }
            recreate();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected String getName() {
        return NAME;
    }

    @Override
    protected String getServiceId() {
        return SERVICE_ID;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        super.onConnected(bundle);
        googleApiClientIsReady = true;
        setState(State.ADVERTISING);
    }

    @Override
    protected void onAdvertisingFailed() {
        super.onAdvertisingFailed();
        setState(State.ERROR);
    }


    @Override
    protected void onConnectionInitiated(Endpoint endpoint, ConnectionInfo connectionInfo) {
        acceptConnection(endpoint);
    }

    @Override
    protected void onEndpointConnected(Endpoint endpoint) {
        super.onEndpointConnected(endpoint);
        setState(State.CONNECTED);
    }

    @Override
    protected void onEndpointDisconnected(Endpoint endpoint) {
        super.onEndpointDisconnected(endpoint);
        if (getConnectedEndpoints().size() == 0) {
            setState(State.ADVERTISING);
        }
        refreshConnectedClients();
    }

    @Override
    protected void onStop() {
        setState(State.UNKNOWN);
        super.onStop();
    }

    public void setIsPublishing(boolean isPublishing) {
        this.isPublishing = isPublishing;
    }

    /**
     * States that the App goes through.
     */
    public enum State {
        UNKNOWN,
        ADVERTISING,
        CONNECTED,
        ERROR

    }

    private void setState(State state) {
        Log.d(TAG, "State set to " + state);
        State oldState = mState;
        mState = state;
        txtState.setText(state.toString()
        );
        onStateChanged(oldState, state);
    }

    private State getState() {
        return mState;
    }

    private void onStateChanged(State oldState, State state) {
        switch (state) {
            case UNKNOWN:
                if(googleApiClientIsReady) {
                    disconnectFromAllEndpoints();
                    stopAdvertising();
                }
                googleApiClientIsReady = false;
                break;
            case ADVERTISING:
                if (googleApiClientIsReady && !isAdvertising())
                    startAdvertising();
                refreshConnectedClients();
                break;
            case CONNECTED:
                refreshConnectedClients();
                break;
            case ERROR:
                break;
            default:
                // no-op
                break;
        }
        txtLog.append("\n");
        txtLog.append("Now in state: " + state.toString());
    }

}
