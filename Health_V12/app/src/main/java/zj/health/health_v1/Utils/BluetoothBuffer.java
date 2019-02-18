//package zj.health.health_v1.Utils;
//
///**
// * Created by Administrator on 2018/6/20.
// */
//
//public class BluetoothBuffer {
//
//    public int _rawBufferSize;
//
//    public  void appendBuffer(byte[] buffer) {
//        if (null == buffer || 0 == buffer.length) return;
//        int size = buffer.length + this._rawBufferSize;
//        if (size <= this._rawBuffer.length) {
//            System.arraycopy(buffer, 0, this._rawBuffer, this._rawBufferSize, buffer.length);
//            this._rawBufferSize += buffer.length;
//        } else {
//            int newSize = this._rawBuffer.length;
//            while (newSize <= size) {
//                newSize *= 1.5;
//            }
//            byte[] newRawBuffer = new byte[newSize];
//            System.arraycopy(this._rawBuffer, 0, newRawBuffer, 0, this._rawBufferSize);
//            this._rawBuffer = newRawBuffer;
//            System.arraycopy(buffer, 0, this._rawBuffer, this._rawBufferSize, buffer.length);
//            this._rawBufferSize += buffer.length;
//        }
//    }
//
//    public  byte[] getFrontBuffer(int size) {
//        if (0 >= size || size > this._rawBufferSize) return null;
//        byte[] buffer = new byte[size];
//        System.arraycopy(this._rawBuffer, 0, buffer, 0, size);
//        return buffer;
//    }
//
//    public  void releaseFrontBuffer(int size) {
//        if (0 >= size || size > this._rawBufferSize) return;
//        System.arraycopy(this._rawBuffer, size, this._rawBuffer, 0, this._rawBufferSize - size);
//        this._rawBufferSize -= size;
//    }
//
//
//    private boolean subPackageOnce(BluetoothBuffer buffer) {
//        if (null == buffer) return false;
//        if (buffer.getBufferSize() >= 14) {
//            byte[] rawBuffer =  buffer.getBuffer();
//            //求包长
//            if (isHead(rawBuffer)){
//                pkgSize = byteToInt(rawBuffer[2], rawBuffer[3]);
//            }else {
//                pkgSize = -1;
//                for (int i = 0; i < rawBuffer.length-1; ++i){
//                    if (rawBuffer[i] == -2 && rawBuffer[i+1] == 1){
//                        buffer.releaseFrontBuffer(i);
//                        return true;
//                    }
//                }
//                return false;
//            }
//            //剥离数据
//            if (pkgSize > 0 && pkgSize <= buffer.getBufferSize()) {
//                byte[] bufferData = buffer.getFrontBuffer(pkgSize);
//                long time = System.currentTimeMillis();
//                buffer.releaseFrontBuffer(pkgSize);
//                //在这处理数据
//                deal something。。。。。
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//
//
//}
