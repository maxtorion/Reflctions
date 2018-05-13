package sample;


public class Testclass {

    private  String polePrywatne = "Jestem polem prywatnym";

    public String getPolePubliczne() {
        return polePubliczne;
    }

    private  String polePubliczne ="Jestem polem publicznym";

    private int poleLiczbowe =10;

    public int getPoleLiczbowe() {
        return poleLiczbowe;
    }

    public void setPoleLiczbowe(int poleLiczbowe) {
        this.poleLiczbowe = poleLiczbowe;
    }
}
