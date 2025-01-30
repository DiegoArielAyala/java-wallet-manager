# Org1
export CSR_NAMES_ORG1="C=CO,ST=Antioquia,L=Medellin,O=Org1,OU=Hyperledger Fabric"

export FABRIC_CA_CLIENT_HOME=../fabric-ca/org1.acme.com/int/clients/admin
fabric-ca-client enroll -u http://admin:adminpw@localhost:7056 --csr.names "$CSR_NAMES_ORG1"

export FABRIC_CA_CLIENT_HOME=../fabric-ca/org1.acme.com/tls-int/clients/admin
fabric-ca-client enroll -u http://admin:adminpw@localhost:7057 --csr.names "$CSR_NAMES_ORG1"

# Org2
export CSR_NAMES_ORG2="C=CL,ST=Santiago,L=Santiago,O=Org2,OU=Hyperledger Fabric"

export FABRIC_CA_CLIENT_HOME=../fabric-ca/org2.acme.com/int/clients/admin
fabric-ca-client enroll -u http://admin:adminpw@localhost:8056 --csr.names "$CSR_NAMES_ORG2"

export FABRIC_CA_CLIENT_HOME=../fabric-ca/org2.acme.com/tls-int/clients/admin
fabric-ca-client enroll -u http://admin:adminpw@localhost:8057 --csr.names "$CSR_NAMES_ORG2"

# Org3
export CSR_NAMES_ORG3="C=MX,ST=Mexico City,L=Mexico City,O=Org3,OU=Hyperledger Fabric"

export FABRIC_CA_CLIENT_HOME=../fabric-ca/org3.acme.com/int/clients/admin
fabric-ca-client enroll -u http://admin:adminpw@localhost:9056 --csr.names "$CSR_NAMES_ORG3"

export FABRIC_CA_CLIENT_HOME=../fabric-ca/org3.acme.com/tls-int/clients/admin
fabric-ca-client enroll -u http://admin:adminpw@localhost:9057 --csr.names "$CSR_NAMES_ORG3"