package li.zoss.bfh.bsc.nearby_sender;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends ConnectionsActivity {

    private String TAG = "MainActivity";

    //Used for ConnectionsActivity
    private static final String SERVICE_ID = "li.zoss.bfh.bsc.appstore";
    private final String NAME = "Sender " + UUID.randomUUID();
    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;

    private State mState = State.UNKNOWN;
    private boolean googleApiClientIsReady = false;
    private boolean isStreaming = false;
    private Spinner spinnerTrainList;
    private SeekBar numDelay;
    private Button btnSendDelay, btnSendNext;
    private final Map<String, Endpoint> endpointsRequestedSound = new HashMap<>();

    private ArrayList<Train> mTrainList = new ArrayList<>();
    private Train mTrain;
    private ListView lstViewTrain;
    private TextView txtCurrDelSend,txtNextStop, txtNextStopRequest,txtNextStopisReq;
    private ArrayList<Station> trainRunAdapter;
    ArrayAdapter adapterRun;
    private ImageView imgAdvertising,imgConnected;


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
        btnSendNext = findViewById(R.id.btnNextStop);
        spinnerTrainList = findViewById(R.id.spinnerTrainList);
        btnSendDelay = findViewById(R.id.btnSendDelay);
        numDelay = findViewById(R.id.sbarDelay);
        lstViewTrain = findViewById(R.id.lstViewTrain);
        txtCurrDelSend=findViewById(R.id.txtCurrDelSend);
        txtNextStop=findViewById(R.id.txtNextStop);
        txtNextStopisReq=findViewById(R.id.txtNextStopisReq);
        txtNextStopRequest=findViewById(R.id.txtNextStopRequest);
        imgAdvertising = findViewById(R.id.imgAdvertising);
        imgConnected = findViewById(R.id.imgConnected);
        btnSendNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTrain.setNextValue();
                publishNextStop(mTrain.getNext());
                txtNextStop.setText(mTrain.getNext().getStationName());
                txtNextStopRequest.setText(mTrain.getNext().getStationRequestStop().toString());
                txtNextStopisReq.setText((mTrain.getNext().getStationRequestStop())?"false":"true");
                setTrainRun(mTrain.getTrainRun());
            }
        });
        btnSendDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishDelay("" + numDelay.getProgress());
            }
        });
        numDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                btnSendDelay.setText("send " + progress + " min");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        numDelay.setProgress(0);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mTrainList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrainList.setAdapter(adapter);
        spinnerTrainList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemSelected: " + mTrainList.get(position));
                mTrain = mTrainList.get(position);
                mTrain.resetTrain();
                if (getState().equals(State.CONNECTED)) {
                    disconnectFromAllEndpoints();
                    setState(State.ADVERTISING);
                }
                setTrainRun(mTrain.getTrainRun());
                txtNextStop.setText("");
                txtNextStopRequest.setText("");
                txtNextStopisReq.setText("");
                txtCurrDelSend.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        trainRunAdapter = new ArrayList<>(mTrain.getTrainRun());

         adapterRun = new TrainRunListAdapter(this.trainRunAdapter,getApplicationContext());

        lstViewTrain.setAdapter(adapterRun);


        setState(State.UNKNOWN);
    }

    private void setTrainRun(ArrayList<Station> trainRun) {
        trainRunAdapter.clear();
        trainRunAdapter.addAll(new ArrayList<>(trainRun));
        adapterRun.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void refreshConnectedClients() {
        if(getConnectedEndpoints().size()>0)
        {

        }

    }


    private void publishNextStop(Station station) {
        if (getState().equals(State.CONNECTED) && !isStreaming) {
            JSONObject jsonObject = NotificationPayload.getNextStopJSON(mTrain.getNext());
            send(Payload.fromBytes(jsonObject.toString().getBytes()));
            ArrayList sendSound = new ArrayList<Integer>();
            sendSound.add(R.raw.jinglepublic);
            if (station.hasStationSound())
                sendSound.add(station.getStationSound());
            if (station.hasAdditionalSound())
                sendSound.addAll(station.getmAdditionalSounds());
            publishNextStop(sendSound);
        } else if (isStreaming) {
            Log.i(TAG, "publishNextStop: already Publishing.");
        }
    }

    private void publishNextStop(ArrayList<Integer> rawRessourcesList) {
        if (getState().equals(State.CONNECTED) && endpointsRequestedSound.size() > 0) {
            try {
                isStreaming = true;
                InputStream sequenceInputStream = null;
                for (int i = 0; rawRessourcesList.size() > 0; i++) {
                    if (i == 0) {
                        if (rawRessourcesList.size() > 1) {
                            sequenceInputStream = new SequenceInputStream(
                                    getResources().openRawResource(rawRessourcesList.get(0)),
                                    getResources().openRawResource(rawRessourcesList.get(1)));
                            rawRessourcesList.remove(0);
                            rawRessourcesList.remove(0);
                        } else {
                            sequenceInputStream = getResources().openRawResource(rawRessourcesList.get(0));
                            rawRessourcesList.remove(0);
                        }
                    } else {
                        sequenceInputStream = new SequenceInputStream(
                                sequenceInputStream,
                                getResources().openRawResource(rawRessourcesList.get(0)));
                        rawRessourcesList.remove(0);
                    }
                }

                send(Payload.fromStream(sequenceInputStream), endpointsRequestedSound.keySet());
                sequenceInputStream.read();

            } catch (IOException e) {
                Log.e(TAG, "publishNextStop: ", e);
            }
        }
    }

    private void publishDelay(String delay) {
        Log.i(TAG, "publishDelay: " + delay);
        if (getState().equals(State.CONNECTED) && !isStreaming) {
            JSONObject jsonObject = NotificationPayload.getDelayJSON(delay);
            send(Payload.fromBytes(jsonObject.toString().getBytes()));
        }
        txtCurrDelSend.setText(delay+" min");
    }


    @Override
    protected void onReceive(Endpoint endpoint, Payload payload) {
        super.onReceive(endpoint, payload);
        if (payload.getType() == Payload.Type.BYTES) {
            try {
                JSONObject jsonObject = new JSONObject(new String(payload.asBytes()));
                Log.i(TAG, "onReceive: " + jsonObject);
                switch (NotType.valueOf((jsonObject.getString("Type")))) {
                    case PUBLISH_NEXT_STOP:
                        break;
                    case REQUEST_STOP:
                        Log.i(TAG, "Requested Stop for: " + jsonObject.get("forStation").toString());
                        if(mTrain.getNext().getStationName().equals(jsonObject.get("forStation").toString()))
                        {
                            txtNextStopisReq.setText("true");
                        }
                        else
                        {
                            Log.i(TAG, "Stop was Requested for Wrong Station! Curr:"+mTrain.getNext().getStationName()+" Received:"+jsonObject.get("forStation").toString());
                        }
                        break;
                    case PUBLISH_DELAY:
                        break;
                    case PUBLISH_COACH_INFO:
                        break;
                    case RESPONSE_TRAIN_INFO:
                        break;
                    case REQUEST_TRAIN_INFO:
                        JSONObject jsonPayload = NotificationPayload.getTrainInfo(mTrain, numDelay.getProgress() + " min");
                        send(Payload.fromBytes(jsonPayload.toString().getBytes()), endpoint);
                        boolean alreadyReceived = false;
                        if (jsonObject.has("coachInfoAlreadyReceived"))
                            alreadyReceived = jsonObject.getBoolean("coachInfoAlreadyReceived");
                        if (!mTrain.getSpecialCoachInfo().isEmpty() && !alreadyReceived) {
                            jsonPayload = NotificationPayload.getSpecialCoachInfo(mTrain);
                            send(Payload.fromBytes(jsonPayload.toString().getBytes()), endpoint);
                        }
                        break;
                    case REQUEST_WITH_SOUND:
                        if (jsonObject.getBoolean("willPlaySound"))
                            endpointsRequestedSound.put(endpoint.getId(), endpoint);
                        else {
                            endpointsRequestedSound.remove(endpoint.getId(), endpoint);
                        }
                        JSONObject jsonPayloadSound = NotificationPayload.soundWillPlay(endpointsRequestedSound.containsKey(endpoint.getId()));
                        send(Payload.fromBytes(jsonPayloadSound.toString().getBytes()), endpoint);
                        refreshConnectedClients();
                        break;
                    case RESPONSE_WITH_SOUND:
                        break;
                }

            } catch (JSONException e) {
                Log.e(TAG, "onReceive: ",e );;
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
        if (getConnectedEndpoints().size() > 6) {
            rejectConnection(endpoint);
            return;
        }
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
        endpointsRequestedSound.remove(endpoint.getId());
        refreshConnectedClients();
    }

    @Override
    protected void onStop() {
        setState(State.UNKNOWN);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        disconnectFromAllEndpoints();
        super.onDestroy();

    }

    @Override
    public void onReceiveUpdate(Endpoint endpoint, PayloadTransferUpdate update) {
        Log.i(TAG, "onReceiveUpdate: " + update.getStatus());
        if (isStreaming
                && update.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
            isStreaming = false;
        }
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
                imgConnected.setVisibility(View.INVISIBLE);
                imgAdvertising.setVisibility(View.VISIBLE);
                refreshConnectedClients();
                break;
            case CONNECTED:
                imgConnected.setVisibility(View.VISIBLE);
                imgAdvertising.setVisibility(View.VISIBLE);
                refreshConnectedClients();
                break;
            case ERROR:
                imgConnected.setVisibility(View.INVISIBLE);
                imgAdvertising.setVisibility(View.INVISIBLE);
                break;
            default:
                // no-op
                break;
        }
    }

}
