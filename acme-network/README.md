# Configuración y ejecución de la red Hyperledger Fabric

## acme-network

Este curso usa el siguiente repositorio:

https://github.com/blockchainempresarial/curso-hyperledger-fabric.git
Asegúrese de tener el control de acceso correcto a este repositorio; si tiene problemas, envíe un correo electrónico a ricardo@blockchainempresarial.com

### Directorio de trabajo

Siga y ejecute los siguientes comandos

```shell
 cd $HOME
 git clone https://github.com/blockchainempresarial/curso-hyperledger-fabric.git
 cd curso-hyperledger-fabric/acme-network

```

### Parámetros globales

Ejecute el siguiente comando para definir parámetros globales en la consola de Linux. (temporales)

```shell
export CHANNEL_NAME=marketplace
export VERBOSE=false
export FABRIC_CFG_PATH=$PWD
```

### Certificados

Siga y ejecute los siguientes comandos para generar certificados utilizando la herramienta de cifrado

Primero, cargue los siguientes archivos de configuración en el directorio de trabajo.
crypto-config.yaml

#### Generar certificados de los pers y orderers

Generar certificado, el siguiente comando creará un directorio de configuración de cifrado que contiene varios certificados y claves para pedidos y pares

```shell
cryptogen generate --config=./crypto-config.yaml

```

#### Generando el bloque Orderer Genesis

Más información aquí:
https://hyperledger-fabric.readthedocs.io/en/release-2.2/configtx.html

Primero, cargue los siguientes archivos de configuración en el directorio de trabajo.
configtx.yaml

Para crear el bloque orderer genesis, es necesario utilizar y ejecutar la herramienta configtxgen
El bloque Génesis es el primer bloque de nuestra cadena de bloques. Se utiliza para iniciar el servicio de ordenamiento y asegura la configuración del canal.

```shell
mkdir channel-artifacts
configtxgen  -profile ThreeOrgsOrdererGenesis 	-channelID system-channel -outputBlock ./channel-artifacts/genesis.block
```

#### Generando transacción de configuración de canal 'channel.tx'

Para la transacción de configuración del canal: channel.tx es la transacción que le permite crear el canal Hyperledger Fabric. El canal es la ubicación donde existe el libro mayor y el mecanismo que permite a los pares unirse a las redes de negocio.

```shell
configtxgen -profile ThreeOrgsChannel -outputCreateChannelTx ./channel-artifacts/channel.tx -channelID $CHANNEL_NAME
```

#### Anchor peers transactions

Las transacciones de los anchos peers especifican el Peer de anclaje de cada organización en este canal. Ejecute los siguientes tres comandos para definir el anchor peer para cada organización

##### Generando el anchor peer update para Org1MSP

```shell
configtxgen -profile ThreeOrgsChannel -outputAnchorPeersUpdate ./channel-artifacts/Org1MSPanchors.tx -channelID $CHANNEL_NAME -asOrg Org1MSP

```

##### Generando anchor peer update para Org2MSP

```shell
configtxgen -profile ThreeOrgsChannel -outputAnchorPeersUpdate ./channel-artifacts/Org2MSPanchors.tx -channelID $CHANNEL_NAME -asOrg Org2MSP
```

##### Generando anchor peer update para Org3MSP

```shell
configtxgen -profile ThreeOrgsChannel -outputAnchorPeersUpdate ./channel-artifacts/Org3MSPanchors.tx -channelID $CHANNEL_NAME -asOrg Org3MSP
```

### Inspecciona los artefactos

Para inspeccionar el channel.tx en formato json, siga las siguientes instrucciones.

```shell
configtxgen --inspectChannelCreateTx ./channel-artifacts/channel.tx >> ./channel-artifacts/channel.tx.json
```

Para ver el archivo de resultados:

```shell
vi channel-artifacts/channel.tx.json

```

Para inspeccionar genesis.block en formato json, siga las siguientes instrucciones.

```shell
configtxgen --inspectBlock ./channel-artifacts/genesis.block >> ./channel-artifacts/genesis.block.json

```

Para ver el archivo de resultados:

```shell
vi channel-artifacts/genesis.block.json

```

### CERTIFICADOS E IDENTIDAD

docker-compose -f docker-compose-root-ca.yaml up -d
cd scripts/ && ./rootca.sh
docker-compose -f docker-compose-int-ca.yaml up -d

- Verificar que el admin esta inscripto:
  fabric-ca-client enroll -u http://admin:adminpw@localhost:7056

- Registro de un nuevo admin:
  cd scripts/
  export CSR_NAMES_ORG1="C=CO,ST=Antioquia,L=Medellin,O=Org1,OU=Hyperledger Fabric"
- Registrar la identidad en la intermediate CA de la org 1:
  export FABRIC_CA_CLIENT_HOME=../fabric-ca/org1.acme.com/int/clients/admin
- Hacemos el registro del admin2:
  fabric-ca-client register --id.name admin2@org1.acme.com --id.secret admin2pw --id.type admin -u http://admin:adminpw@localhost:7056

- Solicitamos sus certificados: (Si trabajamos en la nube, instalamos el fabric-ca-client y apuntamos a la ip del ordenador para obtener los certificados)
- Seleccionamos la carpeta donde queremos guardar los certificados:
  export FABRIC_CA_CLIENT_HOME=../fabric-ca/org1.acme.com/int/clients/admin2@org1.acme.com
- Solicitud de certificados (enrollment): el propio admin2 es quien lo realiza ya que es el unico que en este momento tiene la clave publica para hacerlo. Como todavia no tiene la clave privada, el fabric-ca-client genera el par de llaves y solicita al CA que está en el localhost:7056 el certificado por esa llave publica que acaba de generar.
  fabric-ca-client enroll -u http://admin2@org1.acme.com:admin2pw@localhost:7056 --csr.names "$CSR_NAMES_ORG1"

- Solicitud del certidicado del TLS
- Apuntamos al path del administrador del tls:
  export FABRIC_CA_CLIENT_HOME=../fabric-ca/org1.acme.com/tls-int/clients/admin
- Verifico que el admin esta inscripto:
  fabric-ca-client enroll -u http://admin:adminpw@localhost:7057
- Registramos
  fabric-ca-client register --id.name admin2@org1.acme.com --id.secret admin2pw --id.type admin -u http://admin:adminpw@localhost:7057
- Seleccionamos la carpeta donde queremos guardar los certificados:
  export FABRIC_CA_CLIENT_HOME=../fabric-ca/org1.acme.com/tls-int/clients/admin2@org1.acme.com
- Solicitud de certificados (enrollment): la diferencia con el anterior es que tenemos que especificar hosts (localhost o ip) y especificamos que es un certificado de tls
  fabric-ca-client enroll -u http://admin2@org1.acme.com:admin2pw@localhost:7057 --csr.names "$CSR_NAMES_ORG1" --csr.hosts "admin2@org1.acme.com,localhost" --enrollment.profile tls

- Registro de un peer:
  Mismo procedimiento anterior pero cambiamos los nombres y los tipos a peer

- ERROR al ejecutar identities.sh:
  2025/02/01 12:25:58 [ERROR] Enrollment check failed: either because 'x509 enrollment information does not exist - certFile: /home/diegoayala/curso-hyperledger2/acme-network/fabric-ca/org2.acme.com/int/clients/admin/msp/signcerts/cert.pem keyFile: /home/diegoayala/curso-hyperledger2/acme-network/fabric-ca/org2.acme.com/int/clients/admin/msp/keystore/key.pem' or 'Idemix enrollment information does not exist'
  Error: Enrollment information does not exist. Please execute enroll command first. Example: fabric-ca-client enroll -u http://user:userpw@serverAddr:serverPort

- SOLUCION
- Elimina cualquier inscripción previa defectuosa para org2.acme.com y org3.acme.com:
  rm -rf ~/curso-hyperledger2/acme-network/fabric-ca/org2.acme.com/int/clients/admin/msp
  rm -rf ~/curso-hyperledger2/acme-network/fabric-ca/org2.acme.com/tls-int/clients/admin/msp
  rm -rf ~/curso-hyperledger2/acme-network/fabric-ca/org3.acme.com/int/clients/admin/msp
  rm -rf ~/curso-hyperledger2/acme-network/fabric-ca/org3.acme.com/tls-int/clients/admin/msp

- Inscribir manualmente al admin de cada organización
  export FABRIC_CA_CLIENT_HOME=~/curso-hyperledger2/acme-network/fabric-ca/org2.acme.com/int/clients/admin
  fabric-ca-client enroll -u http://admin:adminpw@localhost:8056

export FABRIC_CA_CLIENT_HOME=~/curso-hyperledger2/acme-network/fabric-ca/org2.acme.com/tls-int/clients/admin
fabric-ca-client enroll -u http://admin:adminpw@localhost:8057 --enrollment.profile tls

export FABRIC_CA_CLIENT_HOME=~/curso-hyperledger2/acme-network/fabric-ca/org3.acme.com/int/clients/admin
fabric-ca-client enroll -u http://admin:adminpw@localhost:9056

export FABRIC_CA_CLIENT_HOME=~/curso-hyperledger2/acme-network/fabric-ca/org3.acme.com/tls-int/clients/admin
fabric-ca-client enroll -u http://admin:adminpw@localhost:9057 --enrollment.profile tls

- Volver a ejecutar identities.sh


- Crear los msp que necesitamos. El orderer en el msp no tiene los tls certs y los tls-int certs y tenemos que juntar los certificados de las CA de identidad con los mismos de la CA del tls
- Necesitamos los msp de todos los componentes de la red, y todos los participantes tienen que tener los msp de los demas. Cada org necesita crear y compartir el channel MSP. Agarramos los ca de root, int, tls root y tls int, y los copiamos en los MSP de las organizaciones

- Generamos channel.tx, genesis.block y los anchors de las orgs
./artifacts.sh

- Subimos el docker-compose de los nodos
docker-compose -f docker-compose-cli-couchdb.yaml up -d

- Crear el canal y hacer el update de los Anchor Peers