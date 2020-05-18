package example.booking.model;

public class RoomSelector implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private boolean kingSizeBed;
    private boolean bathtub;
    private boolean terrace;
    private boolean sofa;
    private boolean desktop;
    private boolean view;
    private boolean openWindow;

    public boolean isKingSizeBed() {
        return kingSizeBed;
    }

    public void setKingSizeBed(boolean kingSizeBed) {
        this.kingSizeBed = kingSizeBed;
    }

    public boolean isBathtub() {
        return bathtub;
    }

    public void setBathtub(boolean bathtub) {
        this.bathtub = bathtub;
    }

    public boolean isTerrace() {
        return terrace;
    }

    public void setTerrace(boolean terrace) {
        this.terrace = terrace;
    }

    public boolean isSofa() {
        return sofa;
    }

    public void setSofa(boolean sofa) {
        this.sofa = sofa;
    }

    public boolean isDesktop() {
        return desktop;
    }

    public void setDesktop(boolean desktop) {
        this.desktop = desktop;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public boolean isOpenWindow() {
        return openWindow;
    }

    public void setOpenWindow(boolean openWindow) {
        this.openWindow = openWindow;
    }

    @Override
    public String toString() {
        return "RoomSelector [kingSizeBed=" + kingSizeBed + ", bathtub=" + bathtub + ", terrace=" + terrace + ", sofa=" + sofa + ", desktop=" + desktop + ", view=" + view + ", openWindow=" + openWindow + "]";
    }

}
