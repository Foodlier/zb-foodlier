import { useState, useEffect } from 'react'
import { useLocation } from 'react-router-dom'
import UserReviewedItem from '../../components/user/UserReviewedItem'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import UserRecipeItem from '../../components/user/UserRecipeItem'
import UserChefInfo from '../../components/user/UserChefInfo'
import * as S from '../../styles/user/ProfileMorePage.styled'

const ProfileMorePage = () => {
  const [title, setTitle] = useState('')
  const location = useLocation()
  const { id, nickname, sort } = location.state

  const review = '받은 요리사 후기'
  const recipe = '올린 게시물'
  const recipeReview = '받은 레시피 후기'

  useEffect(() => {
    switch (sort) {
      case 'review':
        setTitle(review)
        break
      case 'recipe':
        setTitle(recipe)
        break
      case 'recipeReview':
        setTitle(recipeReview)
        break
      default:
        break
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  return (
    <>
      <Header />
      <S.ProfileMoreContainer>
        <S.ProfileMoreTitle>
          {nickname}님이 {title}
        </S.ProfileMoreTitle>
        {title === review && (
          <UserChefInfo
            EA={99}
            nickName={nickname}
            chefMemberId={id}
            onlyReview
            isRow
          />
        )}
        {title === recipe && <UserRecipeItem EA={99} memberId={id} />}
        {title === recipeReview && <UserReviewedItem EA={99} memberId={id} />}
      </S.ProfileMoreContainer>
      <BottomNavigation />
    </>
  )
}

export default ProfileMorePage
