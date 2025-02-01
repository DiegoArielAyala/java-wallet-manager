function createChannelMSP() {
    org=$1

    MSP_PATH=../fabric-ca/$org/msp
    mkdir -p $MSP_PATH
    mkdir $MSP_PATH/cacerts && cp ../fabric-ca/$org/root/ca-cert.pem $MSP_PATH/cacerts/ca-cert.pem
    mkdir $MSP_PATH/intermediatecerts && cp ../fabric-ca/$org/int/ca-cert.pem $MSP_PATH/intermediatecerts/ca-cert.pem
    mkdir $MSP_PATH/tlscacerts && cp ../fabric-ca/$org/tls-root/ca-cert.pem $MSP_PATH/tlscacerts/ca-cert.pem
    mkdir $MSP_PATH/tlsintermediatecerts && cp ../fabric-ca/$org/tls-int/ca-cert.pem $MSP_PATH/tlsintermediatecerts/ca-cert.pem
}



# LINEA 22 EN ADELANTE ATRASANDO EL VIDEO

# Linea 29
function createTLSFolder(){
    org=$1
    name=$2
    type=$3

    TLS_FOLDER_PATH=../fabric-ca/$org/${type}s/$name/tls

    mkdir -p $TLS_FOLDER_PATH
    cp ../fabric-ca/$org/tls-int/ca-chain.pem $TLS_FOLDER_PATH/ca.crt
    cp ../fabric-ca/$org/tls-int/clients/$name/msp/signcerts/cert.pem $TLS_FOLDER_PATH/server.crt
    key=$(find ../fabric-ca/$org/tls-int/clients/$name/msp/keystore -name *_sk)
    cp $key $TLS_FOLDER_PATH/server.key
}

createChannelMSP org1.acme.com
createChannelMSP org2.acme.com
createChannelMSP org3.acme.com
createChannelMSP acme.com

createLocalMSP org1.acme.com peer0.org1.acme.com peer
createTLSFolder org1.acme.com peer0.org1.acme.com peer