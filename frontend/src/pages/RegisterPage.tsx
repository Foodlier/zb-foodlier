import * as R from '../styles/RegisterPage.styled'
import upload from '../assets/profile_upload.svg'
// import { useNavigate } from 'react-router-dom'

const Register = () => {
  //   const navigate = useNavigate()

  return (
    // 회원가입 페이지
    <R.Container>
      <R.Title>회원가입</R.Title>
      <R.Profile>
        <R.ProfileImage src={upload} alt="" />
        <p>사진 업로드</p>
      </R.Profile>
      <R.Form>
        <R.InputContainer>
          <R.InputInfo>닉네임</R.InputInfo>
          <R.Input2 type="text" placeholder="닉네임을 입력해주세요" />
          <R.DetailButton>중복확인</R.DetailButton>
        </R.InputContainer>
        <R.InputContainer>
          <R.InputInfo>이메일</R.InputInfo>
          <R.Input2 type="email" placeholder="이메일을 입력해주세요" />
          <R.DetailButton>인증요청</R.DetailButton>
        </R.InputContainer>
        <R.InputContainer>
          <R.InputInfo>비밀번호</R.InputInfo>
          <R.Input1 type="email" placeholder="비밀번호" />
        </R.InputContainer>
        <R.InputContainer>
          <R.InputInfo>비밀번호 확인</R.InputInfo>
          <R.Input1 type="email" placeholder="비밀번호 재입력" />
        </R.InputContainer>
        <R.InputContainer>
          <R.InputInfo>주소</R.InputInfo>
          <R.Input2 type="email" disabled />
          <R.DetailButton>주소검색</R.DetailButton>
        </R.InputContainer>
        <R.Input1 type="email" placeholder="상세 주소" />
        <R.ConfirmButton type="submit">가입하기</R.ConfirmButton>
      </R.Form>
    </R.Container>
  )
}

export default Register
