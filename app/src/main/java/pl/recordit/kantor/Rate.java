package pl.recordit.kantor;

public class Rate {
    final String currency;
    final String code;
    final double mid;//kwoty w Java przechowujemy raczej w klasie BigDecimal

    public Rate(String currency, String code, double mid) {
        this.currency = currency;
        this.code = code;
        this.mid = mid;
    }

    @Override
    public String toString() {
        return currency +"\t" + code +"\t" + mid;
    }
}
