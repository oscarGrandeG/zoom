/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 */

import React from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  StatusBar,
  Button,
} from 'react-native';

import {Colors} from 'react-native/Libraries/NewAppScreen';
import {initZoom, startMeeting} from './nativeModules';

const publicKey = '1zz6PaIZKi72VvUFYfpZ1CT3iW89XF9bUp3P';
const privateKey = 'VXIXPt98RNZfNhRgPFKFgJKROQxSVxW9Nwtf';

const ZOOM_CONFIG = {
  ZOOM_PUBLIC_KEY: publicKey,
  ZOOM_PRIVATE_KEY: privateKey,
  ZOOM_DOMAIN: 'zoom.us',
  JWT_API_KEY: '',
  JWT_API_SECRET_KEY: '',
};

const userName = 'Ojcal';

const App = () => {
  return (
    <>
      <StatusBar barStyle="dark-content" />
      <SafeAreaView>
        <ScrollView
          contentInsetAdjustmentBehavior="automatic"
          style={styles.scrollView}>
          <View style={styles.body}>
            <Button
              title="Init ZOOM"
              onPress={() =>
                initZoom(
                  ZOOM_CONFIG.ZOOM_PUBLIC_KEY,
                  ZOOM_CONFIG.ZOOM_PRIVATE_KEY,
                  ZOOM_CONFIG.ZOOM_DOMAIN,
                )
              }
            />

            <Button
              title="start meeting"
              onPress={() =>
                startMeeting(
                  userName,
                  ZOOM_CONFIG.ZOOM_PUBLIC_KEY,
                  ZOOM_CONFIG.ZOOM_PRIVATE_KEY,
                )
              }
            />
          </View>
        </ScrollView>
      </SafeAreaView>
    </>
  );
};

const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: Colors.lighter,
  },
  engine: {
    position: 'absolute',
    right: 0,
  },
  body: {
    backgroundColor: Colors.white,
    justifyContent: 'space-around',
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
    color: Colors.black,
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
    color: Colors.dark,
  },
  highlight: {
    fontWeight: '700',
  },
  footer: {
    color: Colors.dark,
    fontSize: 12,
    fontWeight: '600',
    padding: 4,
    paddingRight: 12,
    textAlign: 'right',
  },
});

export default App;
