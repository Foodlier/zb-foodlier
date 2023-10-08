import { useEffect, useState } from 'react'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/user/MyProfilePage.styled'
import useIcon from '../../hooks/useIcon'
import { palette } from '../../constants/Styles'
import StarRating from '../../components/StarRating'
import MyRecipeItem from '../../components/user/MyRecipeItem'
import MyReviewedItem from '../../components/user/MyReviewedItem'

interface Review {
  nickName: string
  imgUrl: string
  rating: number
  content: string
}

interface Chef {
  exp: number
  grade: string
  getReview: Review[]
}

interface UserInfoExam {
  memberId: number
  nickname: string
  receivedHeart: number
  profileUrl: string
  isChef: boolean
  ChefMemberId: number
}

const initialUser: UserInfoExam = {
  memberId: 0,
  nickname: '',
  receivedHeart: 0,
  profileUrl: '',
  isChef: false,
  ChefMemberId: 0,
}

const initialChef: Chef = {
  exp: 0,
  grade: '',
  getReview: [],
}

const MyProfilePage = () => {
  const { IcFavoriteFill, ChefHat, InitialChefImg, InitialUserImg } = useIcon()
  const [user, setUser] = useState(initialUser)
  const [chefInfo, setChefInfo] = useState(initialChef)

  useEffect(() => {
    const USER_INFO_EXAM = {
      memberId: 2,
      nickname: '나는 요리사',
      receivedHeart: 1000,
      profileUrl: '',
      isChef: true,
      ChefMemberId: 2,
    }
    setUser(USER_INFO_EXAM)
    if (USER_INFO_EXAM.isChef) {
      const CHEF_INFO_EXAM = {
        exp: 18,
        grade: '실버',
        getReview: [
          {
            nickName: '나는 요청자1',
            imgUrl: '',
            rating: 4,
            content: '요리사님이 친절하시고 요리도 맛도리있어요!',
          },
          {
            nickName: '나는 요청자2',
            imgUrl: '',
            rating: 3,
            content: '요리사님이 친절하시고 요리도 맛도리있어요!',
          },
          {
            nickName: '나는 요청자3',
            imgUrl: '',
            rating: 3,
            content: '요리사님이 친절하시고 요리도 맛도리있어요!',
          },
        ],
      }
      setChefInfo(CHEF_INFO_EXAM)
    }
  }, [])

  // 요리사님이 받은 최근 후기는 우선적으로 2개만 표시
  const previewList = chefInfo.getReview.slice(0, 2)

  return (
    <>
      <Header />
      <S.MyProfileContainer>
        <S.NormalInfoContainer>
          <S.UserContainer>
            <S.UserImgContainer>
              {user.profileUrl && (
                <S.UserImg src={user.profileUrl} alt="프로필 이미지" />
              )}
              {!user.profileUrl && user.isChef && <InitialChefImg size={6} />}
              {!user.profileUrl && !user.isChef && <InitialUserImg size={6} />}
              {user.isChef && (
                <S.UserIsCooker>
                  <ChefHat size={1.2} />
                  요리사
                </S.UserIsCooker>
              )}
            </S.UserImgContainer>
            <S.UserNickname>{user.nickname}</S.UserNickname>
          </S.UserContainer>
          <S.LikeDiv>
            <IcFavoriteFill size={2.5} color={palette.main} />
            {user.receivedHeart}
          </S.LikeDiv>
        </S.NormalInfoContainer>
        {user.isChef && (
          <>
            <S.MyGradeDiv>
              <S.MyGradeP>{user.nickname}님의 등급</S.MyGradeP>
              <S.Bar>
                {chefInfo.exp && (
                  <S.ExpBar $cookerExp={(chefInfo.exp / 50) * 100} />
                )}
              </S.Bar>
              <S.GradeList>
                <span>B</span>
                <span>S</span>
                <span>G</span>
                <span>P</span>
              </S.GradeList>
            </S.MyGradeDiv>
            <S.ReviewContainer>
              <S.ReviewIntro>
                <S.ReviewTitle>
                  {user.nickname}님의 받은 최근 후기
                </S.ReviewTitle>
                <S.ReviewMoreBtn type="button">+</S.ReviewMoreBtn>
              </S.ReviewIntro>
              <S.ReviewCardList>
                {previewList.map(el => (
                  <S.ReviewCard key={el.nickName}>
                    <S.ReviewTopInfo>
                      <S.ReviewAuthor>
                        <img src={el.imgUrl} alt="프로필 이미지" />
                        <span>{el.nickName}</span>
                      </S.ReviewAuthor>
                      <StarRating rating={el.rating} size={1.6} />
                    </S.ReviewTopInfo>
                    <S.ReviewContent>{el.content}</S.ReviewContent>
                  </S.ReviewCard>
                ))}
              </S.ReviewCardList>
            </S.ReviewContainer>
          </>
        )}

        <S.MyUploadContainer>
          <S.MyUploadIntro>
            <S.MyUploadTitle>{user.nickname}님의 올린 게시물</S.MyUploadTitle>
            <S.UploadMoreBtn type="button">+</S.UploadMoreBtn>
          </S.MyUploadIntro>
          <S.MyRecipeList>
            <MyRecipeItem EA={4} />
          </S.MyRecipeList>
        </S.MyUploadContainer>

        <S.MyReviewedContainer>
          <S.MyReviewedInto>
            <S.MyReviewedTitle>
              {user.nickname}님의 레시피 후기
            </S.MyReviewedTitle>
            <S.ReviewedMoreBtn type="button">+</S.ReviewedMoreBtn>
          </S.MyReviewedInto>
          <S.ReviewedList>
            <MyReviewedItem EA={2} />
          </S.ReviewedList>
        </S.MyReviewedContainer>
      </S.MyProfileContainer>

      <S.SpacingDiv />

      <BottomNavigation />
    </>
  )
}

export default MyProfilePage
