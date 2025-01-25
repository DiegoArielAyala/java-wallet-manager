package main.java.com.blockchainempresarial.hyperledger.fabric.samples.wallet;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Wallet {

    @Property()
    private final Double tokenAmount;

    @Property()
    private final String owner;

    public Double getTokenAmount() {
        return tokenAmount;
    }

    public String getOwner() {
        return owner;
    }

    public Wallet(@JsonProperty("tokenAmount") final Double tokenAmount, @JsonProperty("owner") final String owner) {
        // Usamos el @Json para definir el nombre de la propiedad
        this.tokenAmount = tokenAmount;
        this.owner = owner;
    }

    // Es importante sobreescribir el metodo equals para cuando comparemos wallet
    // contra wallet saber lo que vas a comparar.
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        // Comprobamos que el objeto sea igual a este, que no sea nulo y que la clase
        // coincida

        Wallet other = (Wallet) obj; // Creamos un objeto tipo wallet

        // Hacemos una comparacion uno a uno con deepEquals:
        return Objects.deepEquals(new String[] { getTokenAmount().toString().getOwner() },
                new String[] { other.getTokenAmount(), other.getOwner() });
    }

    // Sobreescribimos hashCode para indicar la manera en que vamos a generar un
    // hash
    @Override
    public int hashCode() {
        return Objects.hash(getTokenAmount(), getOwner());
    }

    // Sobreescribimos el metodo toString para definir como se va a devolver el
    // valor cuando llamemos a este metodo.
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "0" + Integer.toHexString(hashCode()) + " [tokenAmount=" + tokenAmount
                + ", owner=" + owner + "]";
    }

}
