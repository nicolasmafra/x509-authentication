# gera a chave e o certificado da CA

# gera a chave da CA
openssl genrsa -out cakey.pem 4096

# gera o certificado auto assinado da CA
openssl req -x509 -new -key cakey.pem -out ca.cer -days 730 -subj '/CN=CA'



# gera a chave e o certificado do servidor

# gera uma chave para o servidor
openssl genrsa -out serverkey.pem 2048

# gera um certificado sem assinatura para o servidor
openssl req -new -key serverkey.pem -out server.csr -subj '/CN=localhost'

# usa a CA para assinar o certificado do servidor
openssl x509 -req -in server.csr -CA ca.cer -CAkey cakey.pem -CAcreateserial -sha256 -days 365 -out server.cer

# concatena o certificado da CA
cat server.cer ca.cer >> server_chain.pem

# junta a cadeira de certificação com a chave em um p12
openssl pkcs12 -export -in server_chain.pem -inkey serverkey.pem -out server.p12 -password pass:serverpass



# repetição do processo do servidor
# gera a chave e o certificado do cliente

# gera uma chave para o cliente
openssl genrsa -out clientkey.pem 2048

# gera um certificado sem assinatura para o cliente
openssl req -new -key clientkey.pem -out client.csr -subj '/CN=localhost'

# usa a CA para assinar o certificado do cliente
openssl x509 -req -in client.csr -CA ca.cer -CAkey cakey.pem -CAserial ca.srl -sha256 -days 365 -out client.cer

# concatena o certificado da CA
cat client.cer ca.cer >> client_chain.pem

# junta a cadeira de certificação com a chave em um p12
openssl pkcs12 -export -in client_chain.pem -inkey clientkey.pem -out client.p12 -password pass:clientpass



# registra o certificado da CA como confiável
rm truststore.jks
keytool -import -keystore truststore.jks -storepass password -file ca.cer -noprompt