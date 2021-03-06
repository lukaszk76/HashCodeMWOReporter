
public class ConsoleDataRaportPrinter extends DataRaportPrinter {

    public ConsoleDataRaportPrinter(Raport raport) {
        super(raport);
    }

    @Override
    public void printRaport() {
        
        String[][] raportData = this.getRaportObject().getRaport();
        
        System.out.println(this.getRaportName());
        System.out.println("Dane za okres: " + this.getTimeRange());
        
        int longestString = 0;
        
        for (int i = 0; i < raportData.length; i++) {
            if (raportData[i][0].length()  > longestString) {
                longestString = raportData[i][0].length();
            }
        }
        
        String stringFormatFormula = "%-" + (longestString + 3) + "s";
        
        for (String[] record : this.getRaportObject().getRaport()) {
            for (String value : record) {
                if (value.equals(record[0])) {
                    String line = String.format(stringFormatFormula, value);
                    System.out.print(line);
                }
                else {
                    String line = String.format("%-25s", value);
                    System.out.print(line);
                }
            }
            System.out.println();
        }
    }
}