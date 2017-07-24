package cf.connota.sunrinton.Util;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by Conota on 2017-07-24.
 */

public class TOOHardWorkManger implements TextToSpeech.OnInitListener {
    private Context context;
    private TextToSpeech tts;
    private Vibrator vibrator;
    private Ringtone ringtone;
    private String v = "";

    public TOOHardWorkManger(Context context) {
        this.context = context;
        tts = new TextToSpeech(context, this);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        ringtone = RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        ringtone.setStreamType(AudioManager.STREAM_ALARM);

    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            tts.speak(v, TextToSpeech.QUEUE_ADD, null);
        } else if (i == TextToSpeech.ERROR) {
            if (tts != null)
                tts.setLanguage(Locale.ENGLISH);
        }
    }

    public void ringVib() {
        vibrator.vibrate(1000);
    }

    public void ringAlr() {
        ringtone.play();
    }

    public void stopAlr() {
        vibrator.cancel();
        ringtone.stop();
    }

    public void destroyTTS() {
        tts.shutdown();
    }

    public void talk(String s) {
        v = s;
        tts.speak(s, TextToSpeech.QUEUE_ADD, null);
    }

}
