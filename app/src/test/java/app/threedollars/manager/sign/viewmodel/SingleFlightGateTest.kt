package app.threedollars.manager.sign.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class SingleFlightGateTest {

    // TH-1476 TC1
    @Test
    fun `TH1476_TC1_요청중에_다시_진입하면_거절한다`() {
        // Given
        val gate = SingleFlightGate()
        gate.tryEnter()

        // When
        val secondEnter = gate.tryEnter()

        // Then
        assertFalse(secondEnter)
        assertTrue(gate.isRunning.value)
    }

    // TH-1476 TC2
    @Test
    fun `TH1476_TC2_요청이_끝나면_다시_진입할_수_있다`() {
        // Given
        val gate = SingleFlightGate()
        gate.tryEnter()

        // When
        gate.exit()
        val reEnter = gate.tryEnter()

        // Then
        assertTrue(reEnter)
    }

    // TH-1476 TC3
    @Test
    fun `TH1476_TC3_동시에_여러번_진입해도_한번만_통과한다`() {
        // Given
        val gate = SingleFlightGate()
        val passed = AtomicInteger(0)
        val threads = List(16) { Thread { if (gate.tryEnter()) passed.incrementAndGet() } }

        // When
        threads.forEach { it.start() }
        threads.forEach { it.join() }

        // Then
        assertEquals(1, passed.get())
    }
}
