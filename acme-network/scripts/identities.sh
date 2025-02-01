function issueCertificates() { # funcion para emitir certificados
    ca=$1 # Ca que quiero usar
    ca_port=$2 # puerto del ca
    org=$3 # que organizaciones
    id_name=$4 # nombre
    id_secret=$5 # contraseña
    id_type=$6 # peer, order, admin, client
    csr_names=$7 # que campos quiero tener en su sujero como OU
    csr_hosts=$8 # nombres de los host registrando en el certificado, importante en los tls porque es conexion entre servidores, por lo que tiene que estar registrado en el certificado del tls


    # register identity with CA admin : registra nueva identidad a traves del admin que tiene permiso para hacerlo
    export FABRIC_CA_CLIENT_HOME=../fabric-ca/$org/$ca/clients/admin
    fabric-ca-client register --id.name $id_name --id.secret $id_secret --id.type  $id_type -u http://admin:adminpw@localhost:$ca_port
    # enroll registered identity
    export FABRIC_CA_CLIENT_HOME=../fabric-ca/$org/$ca/clients/$id_name # apunto a la carpeta donde quiero que se generen los certificados
    fabric-ca-client enroll -u http://$id_name:$id_secret@localhost:$ca_port --csr.names "$csr_names" --csr.hosts "$csr_hosts"
} # Dependiendo el caso, puede ser que todo esto se realice solo pasando el csr, o con AWS, no se necesita siempre hacer el registro

function issueTLSCertificates() {
    ca=$1 
    ca_port=$2 
    org=$3 
    id_name=$4
    id_secret=$5
    id_type=$6 
    csr_names=$7 
    csr_hosts=$8


    # register identity with CA admin 
    export FABRIC_CA_CLIENT_HOME=../fabric-ca/$org/$ca/clients/admin
    fabric-ca-client register --id.name $id_name --id.secret $id_secret --id.type  $id_type -u http://admin:adminpw@localhost:$ca_port
    # enroll registered identity 
    export FABRIC_CA_CLIENT_HOME=../fabric-ca/$org/$ca/clients/$id_name 
    fabric-ca-client enroll -u http://$id_name:$id_secret@localhost:$ca_port --csr.names "$csr_names" --csr.hosts "$csr_hosts" --enrollment.profile tls
}

# Org 1
export CSR_NAMES_ORG1="C=CO,ST=Antioquia,L=Medellin,O=Org1,OU=Hyperledger Fabric"
# issue certificates for admin user identity
issueCertificates int 7056 org1.acme.com admin@org1.acme.com adminpw admin "$CSR_NAMES_ORG1" ""
issueTLSCertificates tls-int 7057 org1.acme.com admin@org1.acme.com adminpw admin "$CSR_NAMES_ORG1" "admin@org1.acme.com, localhost"
# issue certificates for client identity and for client tls : como estan en la misma maquina, no seria necesario generar un tls por cada tipo de certificado (cliente peer admin)
issueCertificates int 7056 org1.acme.com client@org1.acme.com clientpw client "$CSR_NAMES_ORG1" ""
issueTLSCertificates tls-int 7057 org1.acme.com client@org1.acme.com clientpw client "$CSR_NAMES_ORG1" "client@org1.acme.com, localhost"
# issue certificates for peer node identity and for peer server tls
issueCertificates int 7056 org1.acme.com peer0.org1.acme.com peerpw peer "$CSR_NAMES_ORG1" ""
issueTLSCertificates tls-int 7057 org1.acme.com peer0.org1.acme.com peerpw peer "$CSR_NAMES_ORG1" "peer0.org1.acme.com, localhost"

# Org 2
export CSR_NAMES_ORG2="C=CL,ST=Santiago,L=Santiago,O=Org2,OU=Hyperledger Fabric"
# issue certificates for admin user identity
issueCertificates int 8056 org2.acme.com admin@org2.acme.com adminpw admin "$CSR_NAMES_ORG2" ""
issueTLSCertificates tls-int 8057 org2.acme.com admin@org2.acme.com adminpw admin "$CSR_NAMES_ORG2" "admin@org2.acme.com, localhost"
# issue certificates for client identity and for client tls
issueCertificates int 8056 org2.acme.com client@org2.acme.com clientpw client "$CSR_NAMES_ORG2" ""
issueTLSCertificates tls-int 8057 org2.acme.com client@org2.acme.com clientpw client "$CSR_NAMES_ORG2" "client@org2.acme.com, localhost"
# issue certificates for peer node identity and for peer server tls
issueCertificates int 8056 org2.acme.com peer0.org2.acme.com peerpw peer "$CSR_NAMES_ORG2" ""
issueTLSCertificates tls-int 8057 org2.acme.com peer0.org2.acme.com peerpw peer "$CSR_NAMES_ORG2" "peer0.org2.acme.com, localhost"

# Org 3
export CSR_NAMES_ORG3="C=MX,ST=Mexico City,L=Mexico City,O=Org3,OU=Hyperledger Fabric"
# issue certificates for admin user identity
issueCertificates int 9056 org3.acme.com admin@org3.acme.com adminpw admin "$CSR_NAMES_ORG3" ""
issueTLSCertificates tls-int 9057 org3.acme.com admin@org3.acme.com adminpw admin "$CSR_NAMES_ORG3" "admin@org3.acme.com, localhost"
# issue certificates for client identity and for client tls
issueCertificates int 9056 org3.acme.com client@org3.acme.com clientpw client "$CSR_NAMES_ORG3" ""
issueTLSCertificates tls-int 9057 org3.acme.com client@org3.acme.com clientpw client "$CSR_NAMES_ORG3" "client@org3.acme.com, localhost"
# issue certificates for peer node identity and for peer server tls
issueCertificates int 9056 org3.acme.com peer0.org3.acme.com peerpw peer "$CSR_NAMES_ORG3" ""
issueTLSCertificates tls-int 9057 org3.acme.com peer0.org3.acme.com peerpw peer "$CSR_NAMES_ORG3" "peer0.org3.acme.com, localhost"

# Acme
export CSR_NAMES_ACME="C=BE,ST=Flemish Brabant,L=Louvain,OU=Hyperledger Fabric"
# issue certificates for admin user identity and admin client tls
issueCertificates int 10056 acme.com admin@acme.com adminpw admin "$CSR_NAMES_ACME" ""
issueTLSCertificates tls-int 10057 acme.com admin@acme.com adminpw admin "$CSR_NAMES_ACME" "admin@acme.com, localhost"
# issue certificates for orderer node identity and for orderer server tls
issueCertificates int 10056 acme.com orderer.acme.com ordererpw orderer "$CSR_NAMES_ACME" ""
issueTLSCertificates tls-int 10057 acme.com orderer.acme.com ordererpw orderer "$CSR_NAMES_ACME" "orderer.acme.com, localhost"
# Acme no tiene peer y no necesita un client porque no va a ser un usuario que transacciones en la red