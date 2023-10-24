import styled from 'styled-components'
import {
  breakpoints,
  palette,
  typography,
  weight,
} from '../../constants/Styles'

// 스타일드 컴포넌트
export const Container = styled.div`
  width: 100%;
`

export const Map = styled.div`
  width: 100%;
  height: 440px;
  background-color: teal;
`

export const ChefListContainer = styled.div`
  display: flex;
  flex-direction: column;
  padding: 20px 20px 0 20px;
`
export const Info = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
`
export const SubTitle = styled.span`
  font-weight: ${weight.subTitle};
  font-size: ${typography.mobile.subTitle};
  ${breakpoints.large} {
    font-size: ${typography.web.subTitle};
  }
`

export const OptionList = styled.ul<{ $toggle: boolean }>`
  display: ${props => (props.$toggle ? 'block' : 'none')};
  width: 100%;
  position: absolute;
  top: 30px;
  left: 0;
  background-color: ${palette.white};
  border: 1px solid ${palette.divider};
  padding-left: 0;
  border-radius: 5px;
`

export const Option = styled.li`
  &:hover {
    background-color: ${palette.divider};
  }
`

export const OptionButton = styled.button`
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 100%;
  text-align: right;
  padding: 0 4px;
`
export const CardList = styled.ul`
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 500px;
  overflow: auto;
  padding-left: 0;
  border-bottom: 1px solid ${palette.divider};

  &::-webkit-scrollbar {
    display: none;
  }
`

export const Card = styled.li`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 100%;
  height: 220px;
  padding: 20px;
  box-sizing: border-box;
  margin-bottom: 10px;
  border: 1px solid ${palette.divider};
  border-radius: 10px;
  box-shadow: 0px 2px 6px 2px rgba(89, 97, 104, 0.1);
`
export const CardInfo = styled.div`
  display: flex;
  gap: 1.5rem;
`

export const MainImg = styled.img`
  min-width: 90px;
  height: 90px;
  border-radius: 10px;
`

export const ChefInfo = styled.div`
  display: flex;
  flex-direction: column;
  padding-top: 10px;
  gap: 16px;
`

export const ChefTopInfo = styled.div`
  display: flex;
  align-items: center;
  height: 20px;
  .nickName {
    font-size: ${typography.mobile.content};
    font-weight: bold;
  }
`

export const ChefBottomInfo = styled.p`
  max-width: 188px;
`
export const ElseInfo = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: end;
`

export const RequestButton = styled.button`
  width: 146px;
  height: 44px;
  line-height: 44px;
  text-align: center;
  border: 1px solid ${palette.main};
  color: ${palette.main};
  font-weight: bold;
  border-radius: 5px;
`

export const SpaceDiv = styled.div`
  width: 100%;
  height: 100px;
  ${breakpoints.large} {
    height: 50px;
  }
`
