# JsonRequester
An Android library for sending fast and clean json request, using Volley

##Getting started

### Dependency

```
dependencies {
    compile 'com.alirezaafkar:json-requester:1.0.1'
    compile 'com.mcxiaoke.volley:library:1.0.19'
    compile 'com.android.support:appcompat-v7:23.1.1'
}
```

### Usage

Initialize `Requester` in your `Application` class in the `onCreate()` method.

```java
@Override
public void onCreate() {
    super.onCreate();
    
    Map<String, String> header = new HashMap<>();
    header.put("charset", "utf-8");
    
    Requester.Config config = new Requester.Config(getApplicationContext());
    config.setHeader(header);    
    Requester.init(config);
}
```

### Create a requester
```java
JsonObjectRequester mRequester;
mRequester = new RequestBuilder(this)
            .requestCode(REQUEST_CODE)
            .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
            .showError(true) //Show error with toast on Network or Server error
            .shouldCache(true)
            .priority(Request.Priority.NORMAL)
            .allowNullResponse(true)
            .tag(REQUEST_TAG)
            .addToHeader("token", user_tooken)
            .buildObjectRequester(listener); //or .buildArrayRequester(listener);
```
### Create a listener
```java
private class JsonObjectListener extends Response.SimpleObjectResponse {
@Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
          //Ok
        }

        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
          //Error (Not server or network error)
        }

        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
          //Network or Server error
        }

        @Override
        public void onRequestStart(int requestCode) {
          //Show loading or disable button
        }

        @Override
        public void onRequestFinish(int requestCode) {
          //Hide loading or enable button
        }
}
```

### Request
```java
mRequester.request(Request.Method.GET, your_api);
mRequester.request(Request.Method.POST, api, body); // application/x-www-form-urlencoded
mRequester.request(Request.Method.POST, api, json); // application/json
```

### Callbacks
Set callback to null on destroy

### Activity
```java
@Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            mRequester.setCallback(null);
        }
    }
```

### Fragment
```java
@Override
    public void onDetach() {
        super.onDetach();
        if (isRemoving() || getActivity().isFinishing()) {
            mRequester.setCallback(null);
        }
    }
```

### Nested Fragment
```java
if (getParentFragment().isRemoving() ||
                isRemoving() || getActivity().isFinishing()) {
            mRequester.setCallback(null);
}
```

###Override strings
```xml
<string name="parsing_error">Parsing error</string>
<string name="server_error">Server error</string>
<string name="network_error">Check your connection</string>
<string name="no_connection_error">No Connection</string>
<string name="timeout_error">Timeout error</string>
```

#Licence

    Copyright 2015 Alireza Afkar
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
