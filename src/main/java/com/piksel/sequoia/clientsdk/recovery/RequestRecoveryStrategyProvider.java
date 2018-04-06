package com.piksel.sequoia.clientsdk.recovery;

import com.piksel.sequoia.annotations.PublicEvolving;

@PublicEvolving
public interface RequestRecoveryStrategyProvider {

    RecoveryStrategy getRecoveryStrategy();

}