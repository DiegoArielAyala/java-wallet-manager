package main.java.com.blockchainempresarial.hyperledger.fabric.samples.wallet;

import java.util.EmptyStackException;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ChaincodeException;

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
        OWNER_IS_EMPTY,
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
    // Es importante que el walletId cumpla algun patron ascendente, para facilitar
    // la busqueda
    @Transaction()
    public Wallet createWallet(final Context ctx, final String walletId, final String tokenAmountStr,
            final String owner) {
        // Como es transaccional, tenemos que pasarle como parametro el contexto. Este
        // contexto es parte de Hyperledger
        // Tambien debemos pasar un identificador de la wallet, el amount y owner

        // Debemos hacer validaciones para que los parametros que se reciban sean
        // correctos o coherentes.
        double tokenAmountDouble = 0.0;
        try {
            tokenAmountDouble = Double.parseDouble(tokenAmountStr);
            if (tokenAmountDouble < 0.0) {
                // Valido que tokenAmountDouble sea mayor que 0, sino lanzo un mensaje de error.
                String errorMessage = String.format("Amount %s error", tokenAmountStr);
                throw new ChaincodeException(errorMessage, WalletManagerErrors.AMOUNTFORMAT_ERROR.toString());
            }
        } catch (NumberFormatException e) {
            throw new ChaincodeException(e);
        }

        try {
            if (owner == "") {
                String errorMessage = String.format("Owner from %s is empty", walletId);
                throw new ChaincodeException(errorMessage, WalletManagerErrors.OWNER_IS_EMPTY.toString());
            }
        } catch (EmptyStackException e) {
            throw new ChaincodeException(e);
        }

        // Obtenemos el stub y lo guardo en una variable. Con el contexto (ctx) obtengo
        // el stub
        ChaincodeStub stub = ctx.getStub();

        // Valido si la wallet ya existe:
        String walletState = stub.getStringState(walletId);
        // Con stub puedo acceder al estado de la wallet walletId, por lo que puedo
        // comprobar si existe o no, si está vacio es que no existe. Accedo al estado en
        // formato string (getStringState)

        if (walletState.isEmpty()) {
            String errorMessage = String.format("Wallet %s already exist", walletId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, WalletManagerErrors.WALLET_ALREADY_EXISTS);
        }

        Wallet wallet = new Wallet(tokenAmountDouble, owner);
        // En la blockchain no se guarda una cadena de bytes sino un documento
        // serializado genson
        walletState = genson.serialized(wallet);
        // Guardamos la wallet creando un nuevo stub, con una clave (walletId) y un
        // valor (walletState).
        stub.putStringState(walletId, walletState);

        return wallet;
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
        ChaincodeStub stub = ctx.getStub();
        String walletState = stub.getStringState(walletId);

        if (walletState.isEmpty()) {
            String errorMessage = String.format("Wallet %s does not exists", walletId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, WalletManagerErrors.WALLET_NOT_FOUND.toString());
        }
        // Creo la wallet
        // El estado "walletState" que estaba guardado como String lo deserializo y lo
        // transformo en objeto Wallet y lo devuelvo como parametro de salida de este
        // metodo getWallet.
        Wallet wallet = genson.deserialize(walletState, Wallet.class);
        return wallet;

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
    // Los metodos transaccionales esperan siempre parametros en formato de texto
    @Transaction()
    public String transfer(final Context ctx, final String fromWalletId, final String toWalletId,
            final String tokenAmountStr) {
        // La transferencia se debe realizar punto a punto. La diferencia con un sistema
        // centralizado es que solo ellos son los responsables de mantener la
        // consistencia y seguridad del ledger. En este caso, es responsabilidad de toda
        // la red.

        // Todos los valores que utilizaremos en la funcion, se deben pasar como
        // parametros al inicio, siendo valores ya verificados, para no generar
        // multiples consultas más adelantes a diferentes peers. Asi no se generará un
        // consenso y tendremos un bag

        // Verifico que el tokenAmountStr tiene el formato esperado
        double tokenAmountDouble = 0.0;
        try {
            tokenAmountDouble = Double.parseDouble(tokenAmountStr);
            if (tokenAmountDouble < 0.0) {
                String errorMessage = String.format("Amount %s invalid ", tokenAmountStr);
                System.out.println(errorMessage);
                throw new ChaincodeException(errorMessage, WalletManagerErrors.AMOUNTFORMAT_ERROR.toString());
            }
        } catch (NumberFormatException e) {
            throw new ChaincodeException(e);
        }

        // Validamos que las wallet existen en el libro mayor
        ChaincodeStub stub = ctx.getStub();
        String fromWalletState = stub.getStringState(fromWalletId);
        if (fromWalletState.isEmpty()) {
            String errorMessage = String.format("fromWallet %s does not exist", fromWalletId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, WalletManagerErrors.WALLET_NOT_FOUND.toString());
        }

        String toWalletState = stub.getStringState(toWalletId);
        if (toWalletState.isEmpty()) {
            String errorMessage = String.format("toWallet %s does not exist", toWalletId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, WalletManagerErrors.WALLET_NOT_FOUND.toString());
        }

        Wallet fromWallet = genson.deserialize(fromWalletState.Wallet.class);
        Wallet toWallet = genson.deserialize(toWalletState.Wallet.class);

        // Validamos que la wallet que envía tiene fondos
        // Hacer esto de forma descentralizada sin blockchain seria un desafio
        // importante porque se deberia verificar en cada uno de los ledger el amount, y
        // mientras tanto los estados no se alteren.
        if (fromWallet.getTokenAmount() < tokenAmountDouble) {
            throw new ChaincodeException("Token amount not enough",
                    WalletManagerErrors.TOKENAMOUNTNOTENOUGH.toString());
        }

        // Actualizar los saldos de las wallets
        // Creamos una nueva wallet con el monto que es igual al monto anterior menos el
        // tranferido, y el owner es el mismo
        Wallet newFromWallet = new Wallet(fromWallet.getTokenAmount() - tokenAmountDouble, fromWallet.getOwner());
        Wallet newToWallet = new Wallet(toWallet.getTokenAmount() + tokenAmountDouble, toWallet.getOwner());

        // Actualizamos el estado de la wallet
        // Necesito deserializar ya que tengo que convertir newFromWallet en string
        String newFromWalletState = genson.serialize(newFromWallet);
        String newToWalletState = genson.serialize(newToWallet);
        // putStringState(key, value). Al mismo ID de wallet le proveo un nuevo estado:
        stub.putStringState(fromWalletId, newFromWalletState);
        stub.putStringState(toWalletId, newToWalletState);

        return wallet;
    }
}
