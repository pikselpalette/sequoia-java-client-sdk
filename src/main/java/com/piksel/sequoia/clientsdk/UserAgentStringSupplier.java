package com.piksel.sequoia.clientsdk;

import static com.google.common.base.Suppliers.memoize;

import com.google.common.base.Supplier;
import com.piksel.sequoia.annotations.Internal;
import com.piksel.sequoia.clientsdk.configuration.UserAgentConfigurer;
import com.piksel.sequoia.clientsdk.configuration.UserAgentProvider;

@Internal
public class UserAgentStringSupplier implements Supplier<String> {

    public final Supplier<String> agentSupplier;
    
    public UserAgentStringSupplier(UserAgentConfigurer configurer) {
        agentSupplier = memoize(() -> new UserAgentProvider().provideUserAgent(configurer));
    }
    
    @Override
    public String get() {
        return agentSupplier.get();
    }
    
}