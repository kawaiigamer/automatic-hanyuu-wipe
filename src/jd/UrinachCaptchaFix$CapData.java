import hanyuu.net.HTTPClient;
import hanyuu.net.wipe.AbstractWipe;

class CapData {
    public boolean cap_request = false;

    public AbstractWipe wipe = null;

    public HTTPClient client = null;

    public String key = "";

    public CapData(AbstractWipe wipe) {
        this.wipe = wipe;
    }
}
