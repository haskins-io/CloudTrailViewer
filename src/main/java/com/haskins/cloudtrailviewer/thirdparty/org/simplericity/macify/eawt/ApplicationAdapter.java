package com.haskins.cloudtrailviewer.thirdparty.org.simplericity.macify.eawt;

/*
 * Copyright 2007 Eirik Bjorsnos.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @modifier mark.haskins
 */
public class ApplicationAdapter implements ApplicationListener {

    @Override
    public void handleQuit(ApplicationEvent event) {}

    @Override
    public void handleAbout(ApplicationEvent event) {}

    @Override
    public void handleOpenApplication(ApplicationEvent event) {}

    @Override
    public void handleOpenFile(ApplicationEvent event) {}

    @Override
    public void handlePreferences(ApplicationEvent event) {}

    @Override
    public void handlePrintFile(ApplicationEvent event) {}

    @Override
    public void handleReOpenApplication(ApplicationEvent event) {}
}
