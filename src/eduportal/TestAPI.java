package eduportal;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;

@Api(name = "test", version = "v1")
public class TestAPI {
	
	@ApiMethod(name = "ping", path = "ping", httpMethod = "GET")
	public Text ping () {
		return new Text("ping");
	}
}
