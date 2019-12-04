package dextrous.kor.evv.korevv.constants;

@androidx.annotation.Keep
public class ApiUrl {
    public static final int API_TIMEOUT = 10000;
     //public static final String BaseUrl = "http://admin.korevv.com/api/";
    public static final String BaseUrl = "http://dev.korevv.com/api/";

    //test url for android
 //   public static final String BaseUrl = "http://dev.korevv.com/Android/";

    public static final String system_token = BaseUrl + "system_token";
    public static final String Login = BaseUrl + "user_login";
    public static final String serviceList = BaseUrl + "serviceList";
    public static final String scheduleList = BaseUrl + "scheduleList";
    public static final String billing_list = BaseUrl + "billing_list";
    public static final String checkin_start = BaseUrl + "checkin_start";
    public static final String label_language = BaseUrl + "label_language";
    public static final String appLanguage = BaseUrl + "appLanguage";
    public static final String avail_acitivity = BaseUrl + "avail_acitivity";
    public static final String billing_summary = BaseUrl + "billing_summary";
    public static final String checkout = BaseUrl + "checkout";
    public static final String changePwd = BaseUrl + "changePwd";
    public static final String questionList = BaseUrl + "questionList";
    public static final String verify = BaseUrl + "verify";
    public static final String notification = BaseUrl + "notification";
    public static final String userInfo = BaseUrl + "userInfo";
    public static final String view_notification = BaseUrl + "view_notification";
    public static final String get_help_desk = BaseUrl + "get_help_desk";
    public static final String GET_DOCUMENTS = "userDocsList";
    public static final String GET_DOCUMENTS_TYPE = "docsTypeList";
    public static final String UPLOAD_DOCUMENT = "userDocsInsert";
    public static final String UPLOAD_EXTRA_MINUT = "getExtraRespiteTime";

    public static final String blank_image =
            "iVBORw0KGgoAAAANSUhEUgAAAugAAAOECAYAAADzG8QFAAAABHNCSVQICAgIfAhkiAAAEiJJREFU\n" +
            "    eJzt1jEBACAMwDDAv+dxIoEeiYKe3TMzCwAASDi/AwAAgMegAwBAiEEHAIAQgw4AACEGHQAAQgw6\n" +
            "    AACEGHQAAAgx6AAAEGLQAQAgxKADAECIQQcAgBCDDgAAIQYdAABCDDoAAIQYdAAACDHoAAAQYtAB\n" +
            "    ACDEoAMAQIhBBwCAEIMOAAAhBh0AAEIMOgAAhBh0AAAIMegAABBi0AEAIMSgAwBAiEEHAIAQgw4A\n" +
            "    ACEGHQAAQgw6AACEGHQAAAgx6AAAEGLQAQAgxKADAECIQQcAgBCDDgAAIQYdAABCDDoAAIQYdAAA\n" +
            "    CDHoAAAQYtABACDEoAMAQIhBBwCAEIMOAAAhBh0AAEIMOgAAhBh0AAAIMegAABBi0AEAIMSgAwBA\n" +
            "    iEEHAIAQgw4AACEGHQAAQgw6AACEGHQAAAgx6AAAEGLQAQAgxKADAECIQQcAgBCDDgAAIQYdAABC\n" +
            "    DDoAAIQYdAAACDHoAAAQYtABACDEoAMAQIhBBwCAEIMOAAAhBh0AAEIMOgAAhBh0AAAIMegAABBi\n" +
            "    0AEAIMSgAwBAiEEHAIAQgw4AACEGHQAAQgw6AACEGHQAAAgx6AAAEGLQAQAgxKADAECIQQcAgBCD\n" +
            "    DgAAIQYdAABCDDoAAIQYdAAACDHoAAAQYtABACDEoAMAQIhBBwCAEIMOAAAhBh0AAEIMOgAAhBh0\n" +
            "    AAAIMegAABBi0AEAIMSgAwBAiEEHAIAQgw4AACEGHQAAQgw6AACEGHQAAAgx6AAAEGLQAQAgxKAD\n" +
            "    AECIQQcAgBCDDgAAIQYdAABCDDoAAIQYdAAACDHoAAAQYtABACDEoAMAQIhBBwCAEIMOAAAhBh0A\n" +
            "    AEIMOgAAhBh0AAAIMegAABBi0AEAIMSgAwBAiEEHAIAQgw4AACEGHQAAQgw6AACEGHQAAAgx6AAA\n" +
            "    EGLQAQAgxKADAECIQQcAgBCDDgAAIQYdAABCDDoAAIQYdAAACDHoAAAQYtABACDEoAMAQIhBBwCA\n" +
            "    EIMOAAAhBh0AAEIMOgAAhBh0AAAIMegAABBi0AEAIMSgAwBAiEEHAIAQgw4AACEGHQAAQgw6AACE\n" +
            "    GHQAAAgx6AAAEGLQAQAgxKADAECIQQcAgBCDDgAAIQYdAABCDDoAAIQYdAAACDHoAAAQYtABACDE\n" +
            "    oAMAQIhBBwCAEIMOAAAhBh0AAEIMOgAAhBh0AAAIMegAABBi0AEAIMSgAwBAiEEHAIAQgw4AACEG\n" +
            "    HQAAQgw6AACEGHQAAAgx6AAAEGLQAQAgxKADAECIQQcAgBCDDgAAIQYdAABCDDoAAIQYdAAACDHo\n" +
            "    AAAQYtABACDEoAMAQIhBBwCAEIMOAAAhBh0AAEIMOgAAhBh0AAAIMegAABBi0AEAIMSgAwBAiEEH\n" +
            "    AIAQgw4AACEGHQAAQgw6AACEGHQAAAgx6AAAEGLQAQAgxKADAECIQQcAgBCDDgAAIQYdAABCDDoA\n" +
            "    AIQYdAAACDHoAAAQYtABACDEoAMAQIhBBwCAEIMOAAAhBh0AAEIMOgAAhBh0AAAIMegAABBi0AEA\n" +
            "    IMSgAwBAiEEHAIAQgw4AACEGHQAAQgw6AACEGHQAAAgx6AAAEGLQAQAgxKADAECIQQcAgBCDDgAA\n" +
            "    IQYdAABCDDoAAIQYdAAACDHoAAAQYtABACDEoAMAQIhBBwCAEIMOAAAhBh0AAEIMOgAAhBh0AAAI\n" +
            "    MegAABBi0AEAIMSgAwBAiEEHAIAQgw4AACEGHQAAQgw6AACEGHQAAAgx6AAAEGLQAQAgxKADAECI\n" +
            "    QQcAgBCDDgAAIQYdAABCDDoAAIQYdAAACDHoAAAQYtABACDEoAMAQIhBBwCAEIMOAAAhBh0AAEIM\n" +
            "    OgAAhBh0AAAIMegAABBi0AEAIMSgAwBAiEEHAIAQgw4AACEGHQAAQgw6AACEGHQAAAgx6AAAEGLQ\n" +
            "    AQAgxKADAECIQQcAgBCDDgAAIQYdAABCDDoAAIQYdAAACDHoAAAQYtABACDEoAMAQIhBBwCAEIMO\n" +
            "    AAAhBh0AAEIMOgAAhBh0AAAIMegAABBi0AEAIMSgAwBAiEEHAIAQgw4AACEGHQAAQgw6AACEGHQA\n" +
            "    AAgx6AAAEGLQAQAgxKADAECIQQcAgBCDDgAAIQYdAABCDDoAAIQYdAAACDHoAAAQYtABACDEoAMA\n" +
            "    QIhBBwCAEIMOAAAhBh0AAEIMOgAAhBh0AAAIMegAABBi0AEAIMSgAwBAiEEHAIAQgw4AACEGHQAA\n" +
            "    Qgw6AACEGHQAAAgx6AAAEGLQAQAgxKADAECIQQcAgBCDDgAAIQYdAABCDDoAAIQYdAAACDHoAAAQ\n" +
            "    YtABACDEoAMAQIhBBwCAEIMOAAAhBh0AAEIMOgAAhBh0AAAIMegAABBi0AEAIMSgAwBAiEEHAIAQ\n" +
            "    gw4AACEGHQAAQgw6AACEGHQAAAgx6AAAEGLQAQAgxKADAECIQQcAgBCDDgAAIQYdAABCDDoAAIQY\n" +
            "    dAAACDHoAAAQYtABACDEoAMAQIhBBwCAEIMOAAAhBh0AAEIMOgAAhBh0AAAIMegAABBi0AEAIMSg\n" +
            "    AwBAiEEHAIAQgw4AACEGHQAAQgw6AACEGHQAAAgx6AAAEGLQAQAgxKADAECIQQcAgBCDDgAAIQYd\n" +
            "    AABCDDoAAIQYdAAACDHoAAAQYtABACDEoAMAQIhBBwCAEIMOAAAhBh0AAEIMOgAAhBh0AAAIMegA\n" +
            "    ABBi0AEAIMSgAwBAiEEHAIAQgw4AACEGHQAAQgw6AACEGHQAAAgx6AAAEGLQAQAgxKADAECIQQcA\n" +
            "    gBCDDgAAIQYdAABCDDoAAIQYdAAACDHoAAAQYtABACDEoAMAQIhBBwCAEIMOAAAhBh0AAEIMOgAA\n" +
            "    hBh0AAAIMegAABBi0AEAIMSgAwBAiEEHAIAQgw4AACEGHQAAQgw6AACEGHQAAAgx6AAAEGLQAQAg\n" +
            "    xKADAECIQQcAgBCDDgAAIQYdAABCDDoAAIQYdAAACDHoAAAQYtABACDEoAMAQIhBBwCAEIMOAAAh\n" +
            "    Bh0AAEIMOgAAhBh0AAAIMegAABBi0AEAIMSgAwBAiEEHAIAQgw4AACEGHQAAQgw6AACEGHQAAAgx\n" +
            "    6AAAEGLQAQAgxKADAECIQQcAgBCDDgAAIQYdAABCDDoAAIQYdAAACDHoAAAQYtABACDEoAMAQIhB\n" +
            "    BwCAEIMOAAAhBh0AAEIMOgAAhBh0AAAIMegAABBi0AEAIMSgAwBAiEEHAIAQgw4AACEGHQAAQgw6\n" +
            "    AACEGHQAAAgx6AAAEGLQAQAgxKADAECIQQcAgBCDDgAAIQYdAABCDDoAAIQYdAAACDHoAAAQYtAB\n" +
            "    ACDEoAMAQIhBBwCAEIMOAAAhBh0AAEIMOgAAhBh0AAAIMegAABBi0AEAIMSgAwBAiEEHAIAQgw4A\n" +
            "    ACEGHQAAQgw6AACEGHQAAAgx6AAAEGLQAQAgxKADAECIQQcAgBCDDgAAIQYdAABCDDoAAIQYdAAA\n" +
            "    CDHoAAAQYtABACDEoAMAQIhBBwCAEIMOAAAhBh0AAEIMOgAAhBh0AAAIMegAABBi0AEAIMSgAwBA\n" +
            "    iEEHAIAQgw4AACEGHQAAQgw6AACEGHQAAAgx6AAAEGLQAQAgxKADAECIQQcAgBCDDgAAIQYdAABC\n" +
            "    DDoAAIQYdAAACDHoAAAQYtABACDEoAMAQIhBBwCAEIMOAAAhBh0AAEIMOgAAhBh0AAAIMegAABBi\n" +
            "    0AEAIMSgAwBAiEEHAIAQgw4AACEGHQAAQgw6AACEGHQAAAgx6AAAEGLQAQAgxKADAECIQQcAgBCD\n" +
            "    DgAAIQYdAABCDDoAAIQYdAAACDHoAAAQYtABACDEoAMAQIhBBwCAEIMOAAAhBh0AAEIMOgAAhBh0\n" +
            "    AAAIMegAABBi0AEAIMSgAwBAiEEHAIAQgw4AACEGHQAAQgw6AACEGHQAAAgx6AAAEGLQAQAgxKAD\n" +
            "    AECIQQcAgBCDDgAAIQYdAABCDDoAAIQYdAAACDHoAAAQYtABA";
}
