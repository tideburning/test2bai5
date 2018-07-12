package tide;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import static spark.Spark.get;
import static spark.Spark.port;

public class Server {
    private BigInteger bigInteger = new BigInteger("1");
    private LoadingCache<BigInteger, String> cache;
    private org.slf4j.Logger logger;

    public Server(){
        cache = CacheBuilder.newBuilder().expireAfterWrite(20, TimeUnit.SECONDS).expireAfterAccess(10, TimeUnit.SECONDS).build(new CacheLoader<BigInteger, String>() {
            @Override
            public String load(BigInteger n) throws Exception {
                return GeneratePrime(n);
            }
        });
        logger = LoggerFactory.getLogger(Server.class);

        // port va /prime
        port(8080);
        get("/tide", (request, response)->{
            BigInteger n = new BigInteger(request.queryParams("n"));
            return cache.get(n);
        });
    }

    private String GeneratePrime(BigInteger n){
        StringBuffer res = new StringBuffer();
        BigInteger tmp = bigInteger;
        while(true){
            tmp = tmp.nextProbablePrime();
            if(tmp.compareTo(n) < 0 ) {
                res.append(tmp.toString() + " ");
            } else break;
        }
        return res.toString();
    }
}
