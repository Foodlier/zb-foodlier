import { useSearchParams, useNavigate } from 'react-router-dom'
import * as S from '../../styles/point/Success.styled'
import success from '../../../public/images/success.gif'
import axiosInstance from '../../utils/FetchCall'

function Success() {
  const [searchParams] = useSearchParams()
  const orderId = searchParams.get('orderId')
  const paymentKey = searchParams.get('paymentKey')
  const amount = searchParams.get('amount')
  const cancelReason = 'DoNotWantit'
  const navigate = useNavigate()

  const complete = async () => {
    try {
      const res = await axiosInstance.get(
        `/point/success?orderId=${orderId}&paymentKey=${paymentKey}&amount=${amount}`
      )
      console.log(res)
      navigate('/chat')
    } catch (error) {
      console.log(error)
    }
  }

  const cancel = async () => {
    try {
      const res = await axiosInstance.post(
        `/point/cancel?paymentKey=${paymentKey}&cancelReason=${cancelReason}`
      )
      console.log(res)
      navigate('/chat')
    } catch (error) {
      console.log(error)
      navigate('/chat')
    }
  }

  return (
    <S.SuccessContainer>
      <S.SuccessDiv>
        <S.SuccessImg src={success} alt="결제성공" />
        <S.SuccessMsg>인증 완료</S.SuccessMsg>
      </S.SuccessDiv>
      <S.SuccessPrice>{`결제 금액: ${amount}원`}</S.SuccessPrice>
      <S.ButtonList>
        <S.GoBackButton onClick={complete}>
          결제 완료하고 돌아가기
        </S.GoBackButton>
        <S.CancelButton onClick={cancel}>결제 취소하고 돌아가기</S.CancelButton>
      </S.ButtonList>
    </S.SuccessContainer>
  )
}

export default Success
