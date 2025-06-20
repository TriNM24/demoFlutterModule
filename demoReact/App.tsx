/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

//import { NewAppScreen } from '@react-native/new-app-screen';
import React, { useEffect, useState } from 'react';
import { 
  Button, 
  StatusBar, 
  StyleSheet, 
  Text, 
  useColorScheme, 
  View, 
  Image, 
  ScrollView, 
  Alert, 
  NativeModules,
  NativeEventEmitter
} from 'react-native';

const { NativeBridge } = NativeModules;

// Define the props interface for the component
interface AppProps {
  userId?: string;
  userName?: string;
  message?: string;
  complexData?: string;
}

function App(props: AppProps) {
  const isDarkMode = useColorScheme() === 'dark';
  const [nativeData, setNativeData] = useState<any>({});
  const [loading, setLoading] = useState(true);
  const [eventData, setEventData] = useState<any>(null);
  const [deviceInfo, setDeviceInfo] = useState<any>(null);

  useEffect(() => {
    // Get initial properties from native Android
    const getInitialProperties = () => {
      try {
        console.log('App props received:', props);

        // Extract all data from props
        const receivedData = {
          userId: props.userId,
          userName: props.userName,
          message: props.message,
          complexData: props.complexData
        };

        setNativeData(receivedData);

        console.log('Received data from native Android:', receivedData);
      } catch (error) {
        console.error('Error getting initial properties:', error);

      } finally {
        setLoading(false);
      }
    };

    // Setup event listener cho Native Events
    const eventEmitter = new NativeEventEmitter(NativeBridge);
    const subscription = eventEmitter.addListener('NativeEvent', (event) => {
      setEventData(event);
    });

    getInitialProperties();

    // Cleanup subscription on unmount
    return () => {
      subscription.remove();
    };
  }, [props]);

  const handleButtonPress = () => {
    const dataInfo = `
        Received from Native Android:
        - User ID: ${nativeData.userId || 'Not provided'}
        - User Name: ${nativeData.userName || 'Not provided'}
        - Message: ${nativeData.message || 'Not provided'}
        - Complex Data: ${nativeData.complexData || 'Not provided'}
            `.trim();

    Alert.alert('Native Data Info', dataInfo);
  };

  // Native Bridge Functions
  const showNativeToast = () => {
    NativeBridge.showToast('Hello từ React Native!', 'LONG');
  };

  const getDeviceInfoFromNative = async () => {
    try {
      const info = await NativeBridge.getDeviceInfo();
      setDeviceInfo(info);
      Alert.alert('Thông tin thiết bị', JSON.stringify(info, null, 2));
    } catch (error) {
      Alert.alert('Lỗi', `Không thể lấy thông tin thiết bị: ${error}`);
    }
  };

  if (loading) {
    return (
      <View style={styles.container}>
        <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
        <Text style={styles.loadingText}>Loading data from native Android...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />

      <Text style={styles.title}>Data from Native Android</Text>

      {/* Display other native data */}
      {nativeData.userId && (
        <View style={styles.dataContainer}>
          <Text style={styles.dataText}>User ID: {nativeData.userId}</Text>
          <Text style={styles.dataText}>User Name: {nativeData.userName}</Text>
          <Text style={styles.dataText}>Message: {nativeData.message}</Text>
          {nativeData.complexData && (
            <Text style={styles.dataText}>Complex Data: {nativeData.complexData}</Text>
          )}
        </View>
      )}

      {/* Display event data from native */}
      {eventData && (
        <View style={styles.dataContainer}>
          <Text style={styles.sectionTitle}>Event Data from Native:</Text>
          <Text style={styles.dataText}>{JSON.stringify(eventData, null, 2)}</Text>
        </View>
      )}

      <View style={styles.buttonContainer}>
        <Button title="Show Native Data Info" onPress={handleButtonPress} />
        <Button title="Show Native Toast" onPress={showNativeToast} />
      
      </View>
      <View style={{ marginTop: 10, marginHorizontal: 10}}>
        <Button title="Show Native Dialog" onPress={getDeviceInfoFromNative} />
      </View>
      {deviceInfo && (
        <View style={[styles.dataContainer, { marginTop:10}]}>
          <Text style={styles.deviceInfoTitle}>📱 Thông tin thiết bị:</Text>
          <Text style={styles.deviceInfoText}>Brand: {deviceInfo.brand}</Text>
          <Text style={styles.deviceInfoText}>Model: {deviceInfo.model}</Text>
          <Text style={styles.deviceInfoText}>Android: {deviceInfo.version}</Text>
          <Text style={styles.deviceInfoText}>SDK: {deviceInfo.sdkVersion}</Text>
        </View>
      )}

    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  loadingText: {
    fontSize: 18,
    textAlign: 'center',
    marginTop: 50,
    color: '#666',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    textAlign: 'center',
    marginTop: 50,
    marginBottom: 10,
    color: '#333',
  },
  subtitle: {
    fontSize: 16,
    textAlign: 'center',
    marginBottom: 20,
    color: '#666',
  },
  dataContainer: {
    backgroundColor: 'white',
    marginHorizontal: 20,
    marginBottom: 20,
    padding: 15,
    borderRadius: 10,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  dataText: {
    fontSize: 14,
    marginBottom: 5,
    color: '#333',
  },
  scrollView: {
    flex: 1,
    marginHorizontal: 20,
  },
  scrollContent: {
    paddingBottom: 20,
  },
  imageContainer: {
    marginBottom: 20,
    backgroundColor: 'white',
    borderRadius: 10,
    padding: 10,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  image: {
    width: '100%',
    height: 200,
    borderRadius: 8,
  },
  imageText: {
    textAlign: 'center',
    marginTop: 8,
    fontSize: 14,
    color: '#333',
  },
  buttonContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    marginTop: 20,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
    color: '#333',
  },
});

export default App;
