import { useSearchParams, useNavigate } from 'react-router-dom'
import * as S from '../../styles/point/SuccessPage.styled'
import success from '../../../public/images/success.gif'
import axiosInstance from '../../utils/FetchCall'

const SuccessPage = () => {
  const [searchParams] = useSearchParams()
  const orderId = searchParams.get('orderId')
  const paymentKey = searchParams.get('paymentKey')
  const amount = searchParams.get('amount')
  const navigate = useNavigate()

  const complete = async () => {
    try {
      const res = await axiosInstance.get(
        `/api/point/success?orderId=${orderId}&paymentKey=${paymentKey}&amount=${amount}`
      )
      console.log(res)
      navigate('/')
    } catch (error) {
      console.log(error)
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
      </S.ButtonList>
    </S.SuccessContainer>
  )
}

export default SuccessPage
