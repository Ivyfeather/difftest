package xiangshan.frontend

import chisel3._
import chisel3.util._
import utils.PipelineConnect
import xiangshan._
import xiangshan.utils.XSInfo

class Frontend extends XSModule {
  val io = IO(new Bundle() {
    val backend = new FrontendToBackendIO
  })

  val fakeIFU = Module(new FakeIFU)
  val ibuffer=  Module(new Ibuffer)

  fakeIFU.io.redirect := io.backend.redirect

  ibuffer.io.in <> fakeIFU.io.fetchPacket
  ibuffer.io.flush := io.backend.redirect.valid

  io.backend.cfVec <> ibuffer.io.out

  for(out <- ibuffer.io.out){
    XSInfo(out.fire(),
      p"inst:${Hexadecimal(out.bits.instr)} pc:${Hexadecimal(out.bits.pc)}\n"
    )
  }

}