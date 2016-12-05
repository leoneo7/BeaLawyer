package leoneo7.bealawyer.helper

import android.util.Log
import android.view.MotionEvent
import android.view.View


/**
 * Created by ryouken on 2016/12/02.
 */

//--------------------------------------------------------------------------
// フリックされた方向を算出する
//--------------------------------------------------------------------------
private class FlickTouchListener : View.OnTouchListener {
    // 最後にタッチされた座標
    private var startTouchX: Float = 0.toFloat()
    private var startTouchY: Float = 0.toFloat()

    // 現在タッチ中の座標
    private var nowTouchedX: Float = 0.toFloat()
    private var nowTouchedY: Float = 0.toFloat()

    // フリックの遊び部分（最低限移動しないといけない距離）
    private val adjust = 120f

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        // タッチされている指の本数
        Log.v("motionEvent", "--touch_count = " + event.pointerCount)

        // タッチされている座標
        Log.v("Y", "" + event.y)
        Log.v("X", "" + event.x)

        when (event.action) {

        // タッチ
            MotionEvent.ACTION_DOWN -> {
                Log.v("motionEvent", "--ACTION_DOWN")
                startTouchX = event.x
                startTouchY = event.y
            }

        // タッチ中に追加でタッチした場合
            MotionEvent.ACTION_POINTER_DOWN -> Log.v("motionEvent", "--ACTION_POINTER_DOWN")

        // スライド
            MotionEvent.ACTION_MOVE -> Log.v("motionEvent", "--ACTION_MOVE")

        // タッチが離れた
            MotionEvent.ACTION_UP -> {
                Log.v("motionEvent", "--ACTION_UP")
                nowTouchedX = event.x
                nowTouchedY = event.y

                if (startTouchY > nowTouchedY) {
                    if (startTouchX > nowTouchedX) {
                        if (startTouchY - nowTouchedY > startTouchX - nowTouchedX) {
                            if (startTouchY > nowTouchedY + adjust) {
                                Log.v("Flick", "--上")
                                // 上フリック時の処理を記述する
                            }
                        } else if (startTouchY - nowTouchedY < startTouchX - nowTouchedX) {
                            if (startTouchX > nowTouchedX + adjust) {
                                Log.v("Flick", "--左")
                                // 左フリック時の処理を記述する
                            }
                        }
                    } else if (startTouchX < nowTouchedX) {
                        if (startTouchY - nowTouchedY > nowTouchedX - startTouchX) {
                            if (startTouchY > nowTouchedY + adjust) {
                                Log.v("Flick", "--上")
                                // 上フリック時の処理を記述する
                            }
                        } else if (startTouchY - nowTouchedY < nowTouchedX - startTouchX) {
                            if (startTouchX < nowTouchedX + adjust) {
                                Log.v("Flick", "--右")
                                // 右フリック時の処理を記述する
                            }
                        }
                    }
                } else if (startTouchY < nowTouchedY) {
                    if (startTouchX > nowTouchedX) {
                        if (nowTouchedY - startTouchY > startTouchX - nowTouchedX) {
                            if (startTouchY < nowTouchedY + adjust) {
                                Log.v("Flick", "--下")
                                // 下フリック時の処理を記述する
                            }
                        } else if (nowTouchedY - startTouchY < startTouchX - nowTouchedX) {
                            if (startTouchX > nowTouchedX + adjust) {
                                Log.v("Flick", "--左")
                                // 左フリック時の処理を記述する
                            }
                        }
                    } else if (startTouchX < nowTouchedX) {
                        if (nowTouchedY - startTouchY > nowTouchedX - startTouchX) {
                            if (startTouchY < nowTouchedY + adjust) {
                                Log.v("Flick", "--下")
                                // 下フリック時の処理を記述する
                            }
                        } else if (nowTouchedY - startTouchY < nowTouchedX - startTouchX) {
                            if (startTouchX < nowTouchedX + adjust) {
                                Log.v("Flick", "--右")
                                // 右フリック時の処理を記述する
                            }
                        }
                    }
                }
            }

        // アップ後にほかの指がタッチ中の場合
            MotionEvent.ACTION_POINTER_UP -> Log.v("motionEvent", "--ACTION_POINTER_UP")

        // UP+DOWNの同時発生(タッチのキャンセル）
            MotionEvent.ACTION_CANCEL -> {
                Log.v("motionEvent", "--ACTION_CANCEL")
                Log.v("motionEvent", "--ACTION_OUTSIDE")
            }

        // ターゲットとするUIの範囲外を押下
            MotionEvent.ACTION_OUTSIDE -> Log.v("motionEvent", "--ACTION_OUTSIDE")
        }
        return true
    }
}