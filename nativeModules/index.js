import {NativeModules} from 'react-native';

const {AwesomeZoomSDK} = NativeModules;

console.log('34');
console.log(NativeModules);

async function initZoom(publicKey, privateKey, domain) {
  console.log('340');
  console.log('calling zoom', AwesomeZoomSDK);
  const response = await AwesomeZoomSDK.initZoom(publicKey, privateKey, domain);

  console.log('Response', response);
}

async function startMeeting(username, publicKey, privateKey) {
  const response = await AwesomeZoomSDK.startMeeting(
    username,
    publicKey,
    privateKey,
  );

  console.log('Response - Start Meeting', response);
}

export {initZoom, startMeeting};
