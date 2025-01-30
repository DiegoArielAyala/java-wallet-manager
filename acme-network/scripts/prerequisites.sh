# Scripts para no tener que usar el cli container

sudo apt install gcc
go install github.com/hyperledger/fabric/cmd/configtxgen@latest
go install github.com/hyperledger/fabric/cmd/configtxlator@latest
go install github.com/hyperledger/fabric/cmd/peer@latest 
go install github.com/hyperledger/fabric-ca/cmd/fabric-ca-client@latest 
# ejecutar y agregarlo a $home/.profile (archivo ./bashrc, comando nano ~/.bashrc)
export PATH=$PATH:$GOPATH/bin