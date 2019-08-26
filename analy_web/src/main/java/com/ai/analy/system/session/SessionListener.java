package com.ai.analy.system.session;

public abstract interface SessionListener {
	  public abstract void onAttributeChanged(CacheHttpSession paramRedisHttpSession);

	  public abstract void onInvalidated(CacheHttpSession paramRedisHttpSession);
}
