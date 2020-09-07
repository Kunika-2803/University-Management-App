package kunika.mvnu;

public interface GetOtpInterface {
    void onOtpReceived(String otp);
    void onOtpTimeout();
}