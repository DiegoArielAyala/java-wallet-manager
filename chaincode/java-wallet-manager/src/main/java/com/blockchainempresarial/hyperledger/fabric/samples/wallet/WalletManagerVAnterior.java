package main.java.com.blockchainempresarial.hyperledger.fabric.samples.wallet;

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;

// En el archibo build.gradle, en la clase main, definiamos el nombre de la clase que extendia la clase ChaincodeBase, en este caso WalletManagerVAnterior.
public class WalletManagerVAnterior extends ChaincodeBase {
    // Extendemos el ChaincodeBase que es la forma que se usaba antes

    @Override
    public Response init(ChaincodeStub stub) {
        // Stub es la forma de acceder a los datos del libro mayor para leer o escribir.
        // Dentro de stub viaja la funcion que quiero llamar y los argumentos
        return null;
    }

    @Override
    public Response invoke(ChaincodeStub stub) {
        stub.getStringArgs(); // Obtengo los argumentos de la funcion que estoy invocando
        stub.getFunction(); // Obtengo la funcion que estoy invocando
        // Ademas el stub tiene funciones que permite consultar y crear estados del
        // libro mayor

        if ("create" == stub.getFunction())
            create(null, null);
        else if ("query" == stub.getFunction())

            return null;
    }

    public void create(String[] args, ChaincodeBase stub) {
        return;
    }
}
