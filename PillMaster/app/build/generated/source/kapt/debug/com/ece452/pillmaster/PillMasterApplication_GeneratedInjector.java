package com.ece452.pillmaster;

import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedEntryPoint;

@OriginatingElement(
    topLevelClass = PillMasterApplication.class
)
@GeneratedEntryPoint
@InstallIn(SingletonComponent.class)
public interface PillMasterApplication_GeneratedInjector {
  void injectPillMasterApplication(PillMasterApplication pillMasterApplication);
}
