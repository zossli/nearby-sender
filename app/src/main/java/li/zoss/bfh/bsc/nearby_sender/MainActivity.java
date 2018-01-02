package li.zoss.bfh.bsc.nearby_sender;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.Payload;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class MainActivity extends ConnectionsActivity implements AdapterView.OnItemSelectedListener {

    private String TAG = "MainActivity";

    //Used for ConnectionsActivity
    private static final String SERVICE_ID = "li.zoss.bfh.bsc";
    private final String NAME = "Sender " + UUID.randomUUID();
    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;
    private static final String[] REQUIRED_PERMISSIONS =
            new String[]{
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
    private Intent intent;
    private Spinner spinnerTrainList;
    private SeekBar numDelay;
    private Button btnSendDelay;

    private ArrayList<Train> mTrainList = new ArrayList<>();
    private Train mTrain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        //Create some Trains
        TrainRun trainRun = new TrainRun();
        trainRun.setAll(mTrainList);

        //set current Train
        mTrain = mTrainList.get(0);


        //View
        txtID = findViewById(R.id.txtIDvalue);
        txtState = findViewById(R.id.txtStatevalue);
        txtConnectedClients = findViewById(R.id.txtConnectedDevices);
        txtLog = findViewById(R.id.txtLog);
        btnFloating = findViewById(R.id.floatingActionButton);
        spinnerTrainList = findViewById(R.id.spinnerTrainList);
        btnSendDelay = findViewById(R.id.btnSendDelay);
        numDelay = findViewById(R.id.sbarDelay);


        txtConnectedClients.setText("no Clients connected");
        txtLog.setText("Log:");
        txtID.setText(NAME.substring(0,12)+"...");
        txtState.setText(mState.toString());
        btnFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTrain.setNextValue();
                        publishNextStop(mTrain.getNext());
                    }
                });
            }
        });
        btnSendDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishDelay(""+numDelay.getProgress());
                    }
                });
            }
        });
        numDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                btnSendDelay.setText("send " +progress+" min");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mTrainList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrainList.setAdapter(adapter);
        spinnerTrainList.setOnItemSelectedListener(this);



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



    private void publishNextStop(Station station) {
        if (getState().equals(State.CONNECTED)) {
            JSONObject jsonObject = NotificationPayload.getNextStopJSON(station.getStationName(), station.getStationRequestStop(), mTrain.getNext());
            setIsPublishing(true);
            send(Payload.fromBytes(jsonObject.toString().getBytes()));
            setIsPublishing(false);
        }
    }
    private void publishDelay(String delay) {
        Log.i(TAG, "publishDelay: "+delay);
        if (getState().equals(State.CONNECTED)) {
            JSONObject jsonObject = NotificationPayload.getDelayJSON(delay);
            setIsPublishing(true);
            send(Payload.fromBytes(jsonObject.toString().getBytes()));
            setIsPublishing(false);
        }
    }


    private void publishNextStop(int rawRessource, String type) {
        if (getState().equals(State.CONNECTED)) {

            if (!hasPermissions(this, REQUIRED_PERMISSIONS)) {
                requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
            }
            // Open the ParcelFileDescriptor for this URI with read access.
            ParcelFileDescriptor pfd = null;


            Payload filePayload = null;
            try {
                filePayload = Payload.fromFile(stream2file(getResources().openRawResource(rawRessource)));
            } catch (IOException e) {
                e.printStackTrace();
            }


            //send(Payload.fromBytes(type.getBytes()));
            send(filePayload);

        }
    }
    public static final String PREFIX = "stream2file";
    public static final String SUFFIX = ".tmp";

    public static File stream2file (InputStream in) throws IOException {
        final File tempFile = File.createTempFile(PREFIX, SUFFIX);
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }

    @Override
    protected void onReceive(Endpoint endpoint, Payload payload) {
        super.onReceive(endpoint, payload);
        if(payload.getType() == Payload.Type.BYTES)
        {
            try {
                JSONObject jsonObject = new JSONObject(new String(payload.asBytes()));
                Log.i(TAG, "onReceive: "+jsonObject);
                switch (NotType.valueOf((jsonObject.getString("Type")))){
                    case NEXT_STOP:
                        break;
                    case REQUEST_STOP:
                        txtLog.append("\n");
                        txtLog.append("Requested Stop for: " + jsonObject.get("forStation").toString());
                        break;
                    case DELAY:
                        break;
                    case INFO:
                        break;
                    case TRAIN_INFO:
                        break;
                    case GET_TRAIN:
                        JSONObject jsonPayload = NotificationPayload.getTrainInfo(mTrain.getTrain(),mTrain.getDirection(), mTrain.getNext());
                        send(Payload.fromBytes(jsonPayload.toString().getBytes()), endpoint);
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The user has accepted (or denied) our permission request.
     */
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
            ;
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemSelected: "+mTrainList.get(position));
        mTrain = mTrainList.get(position);
        mTrain.setNextValue(1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.i(TAG, "onNothingSelected: ");
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
                if (googleApiClientIsReady) {
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
