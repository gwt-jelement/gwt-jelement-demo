package gwt.jelement.demo.client;

import com.google.gwt.resources.client.TextResource;
import gwt.jelement.core.Promise;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.speech.SpeechSynthesisUtterance;
import gwt.jelement.speech.SpeechSynthesisVoice;

import java.util.Arrays;
import java.util.function.Predicate;

import static gwt.jelement.Browser.window;

public class SpeechSynthesisDemo extends AbstractDemo {

    @Override
    protected void execute() {
        if (!window.object().has("speechSynthesis")) {
            window.alert("no support for speech synthesis");
            return;
        }
        window.getSpeechSynthesis().cancel();
        getVoicesReady().then($ -> {
            intro();
            return null;
        });
    }

    private void intro() {
        SpeechSynthesisVoice maleUSVoice = getPreferredVoice("en-US",
                voice -> !voice.getName().contains("Zira")
                        && !voice.getName().contains("Female"));
        SpeechSynthesisVoice femaleUSVoice = getPreferredVoice("en-US",
                voice -> voice.getName().contains("Zira")
                        || voice.getName().contains("Female"));
        SpeechSynthesisVoice maleUkVoice = getPreferredVoice("en-GB",
                voice -> !voice.getName().contains("Female"));

        say("Hello. Welcome to the speech synthesis demo.", maleUSVoice);
        say("You sound like a robot. I think I sound better.", femaleUSVoice);
        boolean otherLanguages =
                say("Actually, everything sounds better with a British accent, don't you think?", "en-GB");
        otherLanguages = otherLanguages |
                say("Sur Chrome, nous pouvons parler de nombreuses langues.", "fr");
        otherLanguages = otherLanguages |
                say("Como el español por ejemplo.", "es-US");
        otherLanguages = otherLanguages |
                say("O el típico español de España, soy de Zaragoza.", "es-ES");
        say("Que dices mujer? Que mi español no es típico? , Soy de Monterrey, Mejico!", "es-US");
        otherLanguages = otherLanguages |
                say("Non dimenticare di me! parlo italiano", "it");
        otherLanguages = otherLanguages |
                say("Und ich spreche deutsch.", "de");
        otherLanguages = otherLanguages |
                say("我可以说中文.", "zh");
        otherLanguages = otherLanguages |
                say("나는 한국어를 할 수있다.", "ko");
        otherLanguages = otherLanguages |
                say("И я говорю по-русски.", "ru");
        say("I can even speak English with a French accent. C'est amusant, ça?", "fr");
        say("Il y a aussi d'autres langues, mais ça devient un peu ennuyeux...", "fr");
        say("Ei, espere um pouco, você conseguiu ignorar o português? Bra,sil, Bra, sil, Bra, sil,", "pt");
        if (maleUkVoice != null) {
            say("I want to hear other languages!", maleUkVoice);
        } else {
            say("I want to hear other languages!", "en-GB");
        }
        if (!otherLanguages) {
            say("Guess what, we can only speak American English.", maleUSVoice);
            say("How dumb! I want to be on Chrome. They speak many languages there.", femaleUSVoice);
        } else {
            say("I'm sorry Dave. I'm afraid I can't do that.", maleUSVoice);
        }
    }

    /*
     * https://bugs.chromium.org/p/chromium/issues/detail?id=340160
     * https://dvcs.w3.org/hg/speech-api/raw-file/tip/speechapi-errata.html
     */
    private Promise<Void> getVoicesReady() {
        return new Promise<>((resolve, reject) -> {
            SpeechSynthesisVoice[] voices = window.getSpeechSynthesis().getVoices();
            if (voices.length != 0) {
                resolve.with(null);
            } else if (window.getSpeechSynthesis().object().has("onvoiceschanged")) {
                window.getSpeechSynthesis().setOnVoiceschanged(event -> {
                    if (window.getSpeechSynthesis().getVoices().length != 0) {
                        resolve.with(null);
                    } else {
                        reject.with(new Error("No voices are available."));
                    }
                    return null;
                });
            } else {
                reject.with(new Error("No voices are available."));
            }
        });
    }

    private boolean say(String text, String language) {
        if (isLanguageSupported(language)) {
            SpeechSynthesisUtterance utterance = new SpeechSynthesisUtterance(text);
            utterance.setLang(language);
            window.getSpeechSynthesis().speak(utterance);
            return true;
        }
        return false;
    }

    private boolean isLanguageSupported(String language) {
        return Arrays.stream(window.getSpeechSynthesis().getVoices())
                .anyMatch(voice -> voice.getLang().contains(language));
    }

    private void say(String text, SpeechSynthesisVoice voice) {
        SpeechSynthesisUtterance utterance = new SpeechSynthesisUtterance(text);
        utterance.setVoice(voice);
        window.getSpeechSynthesis().speak(utterance);
    }

    private SpeechSynthesisVoice getPreferredVoice(String language, Predicate<SpeechSynthesisVoice> predicate) {
        SpeechSynthesisVoice[] candidates = Arrays.stream(window.getSpeechSynthesis().getVoices())
                .filter(voice -> voice.getLang().contains(language))
                .toArray(SpeechSynthesisVoice[]::new);
        if (candidates.length != 0) {
            for (SpeechSynthesisVoice candidate : candidates) {
                if (predicate.test(candidate)) {
                    return candidate;
                }
            }
            return candidates[0];
        }
        return null;
    }

    @Override
    void setInactive() {
        window.getSpeechSynthesis().cancel();
        super.setInactive();
    }

    @Override
    protected String getName() {
        return "speech-synth";
    }

    @Override
    protected String getTitle() {
        return "Speech Synthesis Demo";
    }

    @Override
    protected TextResource getTemplate() {
        return HtmlClientBundle.INSTANCE.getSpeechSynthHtml();
    }

    @Override
    protected TextResource getSource() {
        return SourceClientBundle.INSTANCE.getSpeechSynthesisSource();
    }
}
