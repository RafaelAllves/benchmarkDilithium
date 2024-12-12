package org.sample;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.spec.ECGenParameterSpec;
import org.bouncycastle.asn1.x9.X9ECParameters;


import java.math.BigInteger;
import java.security.*;
import java.util.Base64;

public class ECDSA {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final ECDomainParameters CURVE;

    static {
        X9ECParameters ecParams = SECNamedCurves.getByName("secp256k1");
        CURVE = new ECDomainParameters(ecParams.getCurve(), ecParams.getG(), ecParams.getN(), ecParams.getH());
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", "BC");
        keyGen.initialize(new ECGenParameterSpec("secp256k1"));
        return keyGen.generateKeyPair();
    }

    public static BigInteger[] signMessage(byte[] message, PrivateKey privateKey) {
        BigInteger privKey = ((org.bouncycastle.jce.interfaces.ECPrivateKey) privateKey).getD();
        ECPrivateKeyParameters privateKeyParams = new ECPrivateKeyParameters(privKey, CURVE);

        ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
        signer.init(true, privateKeyParams);

        SHA256Digest digest = new SHA256Digest();
        digest.update(message, 0, message.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);

        return signer.generateSignature(hash);
    }

    public static boolean verifySignature(byte[] message, BigInteger[] signature, PublicKey publicKey) {
        ECPoint  pubKey = ((org.bouncycastle.jce.interfaces.ECPublicKey) publicKey).getQ();
        ECPublicKeyParameters publicKeyParams = new ECPublicKeyParameters(pubKey, CURVE);

        ECDSASigner signer = new ECDSASigner();
        signer.init(false, publicKeyParams);

        SHA256Digest digest = new SHA256Digest();
        digest.update(message, 0, message.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);

        return signer.verifySignature(hash, signature[0], signature[1]);
    }

}
