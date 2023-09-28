import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/refrigerator/RequestDetail.styled'

const RequestDetail = () => {
  const REQUEST_EXAM = {
    title: '치킨마요밥 먹고 싶어요',
    content: '치킨마요밥 만들어 주세요',
    ingredients: ['치킨', '마요네즈', '밥'],
    expectedPrice: '5000',
    expectedAt: '18시 30분',
  }

  const TAGGED_EXAM = {
    title: '마크정식',
    like: '100',
    description: '편의점 떡볶이, 소세지와 스트링치즈만 있으면 마크정식 완성!',
  }
  return (
    <>
      <Header />
      <S.RequestContainer>
        <S.RequestHeader>요청서 상세</S.RequestHeader>
        <form action="">
          <S.RequestFormList>
            <li>
              <S.ElementTitle>제목</S.ElementTitle>
              <p>{REQUEST_EXAM.title}</p>
            </li>
            <li>
              <S.ElementTitle>요청자</S.ElementTitle>
              <p>나는 사용자</p>
            </li>
            <li>
              <S.ElementTitle>보유 재료</S.ElementTitle>
              <S.IngredientsBoxContainer>
                {REQUEST_EXAM.ingredients.map(el => (
                  <S.IngredientsBox key={el}>{el}</S.IngredientsBox>
                ))}
              </S.IngredientsBoxContainer>
            </li>
            <li>
              <S.ElementTitle>요청가격</S.ElementTitle>
              <p>{REQUEST_EXAM.expectedPrice}원</p>
            </li>
            <li>
              <S.ElementTitle>요청지역</S.ElementTitle>
              <p>강원도 양구군 박수근로 137</p>
            </li>
            <li>
              <S.ElementTitle>요청시간</S.ElementTitle>
              <p>{REQUEST_EXAM.expectedAt}</p>
            </li>
            <li>
              <S.ElementTitle>요청내용</S.ElementTitle>
              <p>{REQUEST_EXAM.content}</p>
            </li>
            <li>
              <S.ElementTitle>태그된 레시피</S.ElementTitle>
              <S.TaggedRecipe>
                <S.TaggedImg src="" alt="메인 사진" />
                <S.TaggedInfo>
                  <S.TaggedTopInfo>
                    <S.TaggedTitle>{TAGGED_EXAM.title}</S.TaggedTitle>
                    <S.TaggedLike>
                      <img src="" alt="핫뚜" />
                      <span>{TAGGED_EXAM.like}</span>
                    </S.TaggedLike>
                  </S.TaggedTopInfo>
                  <S.TaggedBottomInfo>
                    {TAGGED_EXAM.description}
                  </S.TaggedBottomInfo>
                </S.TaggedInfo>
              </S.TaggedRecipe>
            </li>
          </S.RequestFormList>
          <S.ButtonList>
            <S.RejectButton type="button">거절하기</S.RejectButton>
            <S.AcceptButton type="button">수락하기</S.AcceptButton>
          </S.ButtonList>
        </form>
      </S.RequestContainer>
      <BottomNavigation />
    </>
  )
}

export default RequestDetail
