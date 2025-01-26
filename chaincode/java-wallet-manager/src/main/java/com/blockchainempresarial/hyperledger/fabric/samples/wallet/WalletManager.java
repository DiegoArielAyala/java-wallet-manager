package main.java.com.blockchainempresarial.hyperledger.fabric.samples.wallet;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;

import com.owlike.genson.Genson;

// Definimos varios parametros:
@Contract(name = "WalletManager", info = @Info(title = "WalletManager contract", description = "WalletManager sample contract", version = "0.0.1-SNAPSHOT", license = @License(name = "Apache 2.0 License", url = "http://www.apache.org/licenses/LICENSE-2.0.html"), contract = @Contact(email = "diego@diego.com", name = "F Wallet", url = "http://hyperledger.blockchainempresarial.com")))

// En la version nueva, ya no se usa stub sino que una interfaz que reemplaza el
// extender la clase ChaincodeBase.
// Definimos esta como la clase por defecto
@Default
public final class WalletManager implements ContractInterface {

    // Agrego objeto genson para poder hacer el Marshall de objeto Java a documento
    // Json
    private final Genson genson = new Genson();

    // Creamos un enumerador para tener un listado de errores comunes
    private enum WalletManagerErrors {
        WALLET_NOT_FOUND,
        WALLET_ALREADY_EXISTS,
        AMOUNTFORMAT_ERROR,
        TOKENAMOUNTNOTENOUGH,
    }

    // En el patron de diseño anterior, teniamos el metodo init, pero en este patron
    // hay un metodo iLedger (es opcional), donde se pueden definir distintos
    // elementos
    @Transaction
    public void initLedger(final Context ctx) {
    }

    /**
     * User wallet creation
     * 
     * @param ctx
     * @param walletId
     * @param tokenAmountStr
     * @param owner
     * @return
     */
    @Transaction()
    public Wallet createWallet(final Context ctx, final String walletId, final String tokenAmountStr,
            final String owner) {
        // Como es transaccional, tenemos que pasarle como parametro el contexto. Este
        // contexto es parte de Hyperledger
        // Tambien debemos pasar un identificador de la wallet, el amount y owner
        return null;
    }

    // Creamos el metodo para consultar
    /**
     * User wallet query
     * 
     * @param ctx
     * @param walletId
     * @return
     */
    @Transaction()
    public Wallet getWallet(final Context ctx, final String walletId) {
        return null;
    }

    // Metodo para transferir
    /**
     * Transfer
     * 
     * @param ctx
     * @param fromWalletId
     * @param toWalletId
     * @param tokenAmountStr
     * @return
     */
    @Transaction()
    public String transfer(final Context ctx, final String fromWalletId, final String toWalletId,
            final String tokenAmountStr) {
        return null;
    }
}
