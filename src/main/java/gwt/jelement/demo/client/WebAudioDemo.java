package gwt.jelement.demo.client;

import com.google.gwt.resources.client.TextResource;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.events.Event;
import gwt.jelement.webaudio.*;

import static gwt.jelement.Browser.document;

public class WebAudioDemo extends AbstractDemo {

    private AudioContext audioContext;
    private OscillatorNode osc;
    private GainNode gain;
    private OscillatorNode osc2;
    private boolean playing;
    private BootstrapButton btnSoundOn;
    private BootstrapButton btnSoundOff;

    @Override
    protected void execute() {
        /*
            Audio Synthesis in JavaScript
            Brian Rinaldi
            https://modernweb.com/audio-synthesis-in-javascript/
        */
        btnSoundOn = new BootstrapButton("Play", BootstrapButton.ButtonStyle.SUCCESS, "play");
        btnSoundOn.appendTo(document.getElementById("audio-on"));

        btnSoundOff = new BootstrapButton("Stop", BootstrapButton.ButtonStyle.DANGER, "stop");
        btnSoundOff.setDisabled(true);
        btnSoundOff.appendTo(document.getElementById("audio-off"));

        btnSoundOn.addClickListener((Event event) -> {
            audioContext = new AudioContext();
            osc = audioContext.createOscillator();
            osc.setType(OscillatorType.SINE);
            osc.connect(audioContext.getDestination(), 0);
            osc.getFrequency().setValue(450);

            gain = audioContext.createGain();
            gain.getGain().setValue(100);
            gain.connect(osc.getFrequency());

            osc2 = audioContext.createOscillator();
            osc2.getFrequency().setValue(1.1f);
            osc2.connect(gain);

            osc.start();
            osc2.start();

            btnSoundOn.setDisabled(true);
            btnSoundOff.setDisabled(false);
            playing = true;
        });
        btnSoundOff.addClickListener(event -> {
            stopPlaying();
        });
    }

    private void stopPlaying() {
        osc.stop(audioContext.getCurrentTime() + 1);
        osc2.stop();
        osc.disconnect();
        osc2.disconnect();
        gain.disconnect();
        if (audioContext.getState() != AudioContextState.CLOSED) {
            audioContext.close();
        }
        btnSoundOff.setDisabled(true);
        btnSoundOn.setDisabled(false);
        playing = false;
    }

    @Override
    protected void setIntactive() {
        if (playing) {
            stopPlaying();
        }
        super.setIntactive();
    }

    @Override
    protected String getTitle() {
        return "Web Audio Demo";
    }

    @Override
    protected TextResource getTemplate() {
        return HtmlClientBundle.INSTANCE.getWebAudioHtml();
    }

    @Override
    protected TextResource getSource() {
        return SourceClientBundle.INSTANCE.getWebAudioDemoSource();
    }

    @Override
    protected String getName() {
        return "webaudio";
    }
}
