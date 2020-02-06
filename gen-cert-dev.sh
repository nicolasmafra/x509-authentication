# gera um certificado, exportando-o e exportando sua chave privada
openssl req -x509 -newkey rsa:2048 -out cert-dev.pem -keyout key-dev.pem -days 365 -nodes -subj "/CN=localhost:8080"
# exporta um keystore do tipo pkcs12 junto com a chave privada
openssl pkcs12 -export -in cert-dev.pem -inkey key-dev.pem -out cert-dev.p12 -passout pass:password
# apaga a chave privada
rm key-dev.pem

# apaga trusstore existente
rm truststore.jks
keytool -import -keystore truststore.jks -storepass password -file cert-dev.pem -noprompt
