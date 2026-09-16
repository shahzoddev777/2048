package shahzod.proyekts.game2048


import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class MyTouchListener(context: Context) : View.OnTouchListener {
    private val detector = GestureDetector(context, MyGestureDetector())
    private var moveSideListener: ((SideEnum) -> Unit) ?= null

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        detector.onTouchEvent(event)
        return true
    }

    inner class MyGestureDetector : GestureDetector.SimpleOnGestureListener() {

        override fun onFling(start: MotionEvent?, end: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (start == null) return true
            if (abs(start.x - end.x) < 200 && abs(start.y - end.y) < 200)
                return true

            if (abs(start.x - end.x) > abs(start.y - end.y)) {
                // horizontal
                if (end.x > start.x)  {
                    // right
                    moveSideListener?.invoke(SideEnum.RIGHT)
                } else  {
                    // left
                    moveSideListener?.invoke(SideEnum.LEFT)
                }
            } else {
                // vertical
                if (end.y > start.y) {
                    // down
                    moveSideListener?.invoke(SideEnum.DOWN)
                } else  {
                    // up
                    moveSideListener?.invoke(SideEnum.UP)
                }
            }
            return true
        }
    }

    fun setMoveSideListener(block : (SideEnum) -> Unit) {
        moveSideListener = block
    }
}